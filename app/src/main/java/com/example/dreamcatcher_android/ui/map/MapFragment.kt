package com.example.dreamcatcher_android.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentMapBinding
import com.example.dreamcatcher_android.domain.model.response.SpotPosition
import com.example.dreamcatcher_android.ui.MainViewModel
import com.example.dreamcatcher_android.util.GlobalApplication
import com.example.dreamcatcher_android.util.PermissionCheckList
import com.example.dreamcatcher_android.util.dialog.DialogClickListener
import com.example.dreamcatcher_android.util.dialog.StoryDialogFragment
import com.example.dreamcatcher_android.util.navigateSafe
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMap.LAYER_GROUP_BUILDING
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback, DialogClickListener {

    // 실시간 위치 정보 얻기 위한 클라이언트
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource // 현재 위치 표시, 추적
    private lateinit var naverMap: NaverMap

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var storyDialogFragment: StoryDialogFragment

    // 서버 전송을 위한 Handler
    private val handler = Handler(Looper.getMainLooper())
    private val interval = 10000L // 5초
    private var currentLocation: LatLng? = null // 현재 좌표를 저장할 객체
    private var isDialogVisible = false
    private var questId = 0

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null) // 핸들러에 예약된 모든 작업을 정지
        if (::storyDialogFragment.isInitialized && storyDialogFragment.isAdded) {
            storyDialogFragment.dismissAllowingStateLoss()
        }
    }

    override fun onStop() {
        super.onStop()
        stopTracking() // 사용자의 위치 추적 중단
    }

    override fun setLayout() {
        initSettings()

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로 가기 버튼이 눌렸을 때 앱을 종료
                requireActivity().finishAffinity() // 현재 액티비티와 관련된 모든 액티비티 종료
            }
        })
    }

    private fun initSettings() {
        initButtons()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        if (arePermissionsGranted()) {
            initMap()
        } else {
            Log.d("HomeFragment", "Location permission is not granted.")
        }
    }

    // 권한이 허용되었는지 확인하는 함수
    private fun arePermissionsGranted(): Boolean {
        val permissions = PermissionCheckList.permissions
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun initButtons() {
        binding.fragmentHomeStoryBtn.setOnClickListener {
            val action = MapFragmentDirections.actionMapFragmentToBadgeFragment()
            findNavController().navigateSafe(action.actionId)
        }
    }

    // 네이버 맵 동적으로 불러오기
    private fun initMap() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.fragment_home_map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.fragment_home_map_view, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 초기 좌표 설정
        val dccLatLng = LatLng(36.3752259, 127.3918897) // 대전 DCC 좌표
        val cameraUpdate = CameraUpdate.scrollTo(dccLatLng)
        naverMap.moveCamera(cameraUpdate)

        naverMap.let {
            it.locationSource = locationSource // 내장 위치 추적 기능 사용
            it.locationTrackingMode = LocationTrackingMode.None // 현재 위치 추적 안함
            it.setLayerGroupEnabled(LAYER_GROUP_BUILDING, true) // 레이어 그룹
            it.isIndoorEnabled = true // 실내 지도
        }
        naverMap.uiSettings.let {
            it.isCompassEnabled = true
            it.isScaleBarEnabled = true
            it.isIndoorLevelPickerEnabled = true
            it.isScaleBarEnabled = true
        }
        val locationButton = binding.locationBtn
        locationButton.map = naverMap

        getMarkers() // 마커 정보 서버에서 불러와 표시 및 클릭 리스너 설정
        requestLocationUpdates() // 사용자의 현재 위치 업데이트
        startTracking() // 사용자 위치 추적 및  서버에 현재 위치 전송
    }

    // 사용자의 위치를 추적하는 함수
    private fun startTracking() {
        // 5초마다 현재 위치를 서버로 전송하는 핸들러
        handler.postDelayed(object : Runnable {
            override fun run() {
                sendLocationToServer() // 현재 위치를 서버로 전송하는 함수 호출
                handler.postDelayed(this, interval) // 다시 2초 후에 실행
            }
        }, interval)
    }

    // 서버에 현재 위치 전송하는 함수
    private fun sendLocationToServer() {
        currentLocation?.let { location ->
            val userX = 36.3752259 //   location.latitude
            val userY = 127.3918897 //location.longitude

            // 서버에 현재 위치 전송
            lifecycleScope.launch {
                mainViewModel.getSpotTracking(GlobalApplication.prefsManager.getUserId(),1, userX, userY)

                mainViewModel.questPopupResponse.collect { res ->
                    val questId = res.body()?.questId?.toInt()

                    if(questId != null && questId != 0) { // 퀘스트가 존재하면
                        Log.d("MapFragment", "퀘스트가 존재함, 다이얼로그 표시 ${res.body()}")
                        handler.removeCallbacksAndMessages(null)  // 핸들러 정지
                        observeQuestPopupResponse() // 다이얼로그를 표시하는 함수 호출
                    } else {
                        Log.d("MapFragment", "퀘스트 없음, 핸들러 계속 실행")
                    }
                }
            }

        } ?: Log.d("MapFragment", "현재 위치를 가져올 수 없습니다.")
    }

    // 사용자의 현재 위치를 업데이트하는 함수
    private fun requestLocationUpdates() {
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 4000 // 4초마다 업데이트 요청
        ).build()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // 위치 업데이트시 호출되는 콜백 함수
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.let {
                for (location in it.locations) {
                    // 현재 위치 업데이트
                    currentLocation = LatLng(location.latitude, location.longitude)
                    Log.d("MapFragment", "위치 업데이트: ${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    // 서버에서 마커 좌표 받아오는 함수
    private fun getMarkers() {
        lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.getSpotList(1)
                mainViewModel.spotPositionResponse.collect { res ->
                    if (res.isSuccessful) {
                        val spotList = res.body()?.spotPositionDtoList ?: emptyList()
                        addMarkerToMap(spotList)
                    }
                }
            }
        }
    }

    // 좌표 배열의 좌표로 마커 생성 및 마커 클릭 이벤트 처리 함수
    private fun addMarkerToMap(locations: List<SpotPosition>) {
        for (spot in locations) {
            val posX = spot.posX
            val posY = spot.posY

            // 좌표가 null이 아닌지, NaN이 아닌지 확인한 후에만 마커 추가
            if (posX != null && posY != null && !posX.isNaN() && !posY.isNaN()) {
                val latLng = LatLng(posY, posX) // Latitude는 posY, Longitude는 posX
                val marker = Marker() // 마커 생성
                marker.icon = OverlayImage.fromResource(R.drawable.ic_maker_logo)
                marker.position = latLng
                marker.map = naverMap

                Log.d("MapFragment", "마커 추가됨: ${latLng.latitude}, ${latLng.longitude}, ID: ${spot.id}")

                marker.setOnClickListener { overlay: Overlay ->
                    mainViewModel.clickMarker(GlobalApplication.prefsManager.getUserId(),1, posY, posX)
                    observeQuestPopupResponse()
                    false
                }
            } else {
                Log.e("MapFragment", "Invalid coordinates for marker: posX=$posX, posY=$posY")
            }
        }
    }

    // 서버에서 값 가져와 다이얼로그 생성 및 표시하는 함수 (값 바인딩)
    private fun observeQuestPopupResponse() {
        lifecycleScope.launch {
            mainViewModel.questPopupResponse.collect { res ->
                if (res.isSuccessful && res.body() != null) {
                    val questResponse = res.body()
                    questResponse?.let {
                        questId = it.questId?.toInt() ?: 1

                        // 이미 다이얼로그가 표시 중인지 확인
                        if (::storyDialogFragment.isInitialized && storyDialogFragment.isAdded) {
                            Log.d("MapFragment", "다이얼로그가 이미 표시 중입니다.")
                            return@collect
                        }

                        // 다이얼로그 생성 및 표시 전 핸들러 정지
                        if (!isDialogVisible) {
                            handler.removeCallbacksAndMessages(null)
                            isDialogVisible = true // 다이얼로그 표시 상태 업데이트
                        }

                        // 다이얼로그 생성 및 표시
                        storyDialogFragment = StoryDialogFragment(
                            it.questImg.toString(),
                            it.questName.toString(),
                            it.questDescription.toString(),
                            this@MapFragment // 현재 Fragment를 전달
                        )
                        storyDialogFragment.show(childFragmentManager, "StoryDialogClick")
                    }
                } else {
                    Log.e("MapFragment", "Failed to receive data.")
                }
            }
        }
    }

    private fun stopTracking() {
        handler.removeCallbacksAndMessages(null)
    }

    // 다이얼로그 클릭 (스토리 진행)
    override fun onBtnClick1() {
        val bundle = Bundle().apply {
            putInt("questId", questId)
        }
        if(questId != 0) {
            storyDialogFragment.dismissAllowingStateLoss() // 다이얼로그 닫기
            isDialogVisible = false // 다이얼로그 상태 업데이트
            stopTracking()
            val action = MapFragmentDirections.actionMapFragmentToStoryFragment()
            findNavController().navigateSafe(action.actionId, bundle)
        }
    }

    // 다이얼로그 닫히면 호출되는 함수
    override fun onDialogDismiss() {
        isDialogVisible = false
        startTracking() // 다이얼로그가 닫힐 때 핸들러 다시 시작
    }

}