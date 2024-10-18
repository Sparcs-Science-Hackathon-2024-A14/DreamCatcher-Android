package com.example.dreamcatcher_android.ui.map

import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.dreamcatcher_android.R
import com.example.dreamcatcher_android.base.BaseFragment
import com.example.dreamcatcher_android.databinding.FragmentMapBinding
import com.example.dreamcatcher_android.util.PermissionCheckList
import com.example.dreamcatcher_android.util.navigateSafe
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {

    // 실시간 위치 정보 얻기 위한 클라이언트
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource // 현재 위치 표시, 추적
    private lateinit var naverMap: NaverMap

    // 서버 전송을 위한 Handler
    private val handler = Handler(Looper.getMainLooper())
    private val interval = 2000L // 2초

    // 서버에서 받은 마커 좌표 저장하는 리스트
    //private val locationList = mutableListOf()

    // 좌표 배열의 좌표로 마커 생성 및 마커 클릭 이벤트 처리 함수
    private fun addMarkerToMap(locations: List<LatLng>) {
        for (location in locations) {
            val marker = Marker()
            marker.position = location
            marker.map = naverMap

            marker.setOnClickListener { overlay: Overlay ->

                false
            }
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun setLayout() {
        initSettings()
    }

    private fun initSettings() {
        initButtons()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        if (arePermissionsGranted()) { initMap() }
        else { Log.d("HomeFragment", "Location permission is not granted.") }
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

    // 권한이 허용되었는지 확인하는 함수
    private fun arePermissionsGranted(): Boolean {
        val permissions = PermissionCheckList.permissions
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
        }
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
            it.isLocationButtonEnabled = true
            it.isCompassEnabled = true
            it.isScaleBarEnabled = true
            it.isLocationButtonEnabled = true
            it.isIndoorLevelPickerEnabled = true
            it.isScaleBarEnabled = true
        }

        val maker = Marker()
        maker.position = LatLng(
            naverMap.cameraPosition.target.latitude,
            naverMap.cameraPosition.target.longitude,
        )
        maker.icon = OverlayImage.fromResource(R.drawable.ic_maker_logo) // 마커 아이콘 설정
        maker.map = naverMap // 지도에 마커 추가
    }
}