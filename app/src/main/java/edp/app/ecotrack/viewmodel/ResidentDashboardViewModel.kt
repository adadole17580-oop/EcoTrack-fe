
package edp.app.ecotrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edp.app.ecotrack.data.model.ResidentDashboardResponse
import edp.app.ecotrack.data.repository.ResidentDashboardRepository
import edp.app.ecotrack.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResidentDashboardViewModel : ViewModel() {

    private val repository = ResidentDashboardRepository()

    private val _dashboard = MutableStateFlow<ResidentDashboardResponse?>(null)
    val dashboard: StateFlow<ResidentDashboardResponse?> = _dashboard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _hasUnreadNotifications = MutableStateFlow(false)
    val hasUnreadNotifications: StateFlow<Boolean> = _hasUnreadNotifications.asStateFlow()

    fun loadDashboard(accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _dashboard.value = repository.getDashboard(accessToken)
                loadUnreadNotifications(accessToken)
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message ?: "Unable to load resident dashboard."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUnreadNotifications(accessToken: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.getResidentNotifications(accessToken)
                }
                val array = response.optJSONArray("notifications")
                var unreadCount = 0
                if (array != null) {
                    for (i in 0 until array.length()) {
                        val item = array.optJSONObject(i)
                        if (item != null && !item.optBoolean("is_read", false)) {
                            unreadCount++
                        }
                    }
                }
                _hasUnreadNotifications.value = unreadCount > 0
            } catch (_: Exception) {
            }
        }
    }

    fun updateResidentProfile(
        fullName: String,
        phone: String,
        address: String,
        barangay: String
    ) {
        val currentDashboard = _dashboard.value ?: return

        _dashboard.value = currentDashboard.copy(
            user = currentDashboard.user.copy(
                full_name = fullName,
                phone = phone,
                address = address,
                barangay = barangay
            )
        )
    }
}
