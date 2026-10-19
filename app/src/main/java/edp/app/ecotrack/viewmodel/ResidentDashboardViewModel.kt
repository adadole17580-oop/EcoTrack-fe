
package edp.app.ecotrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edp.app.ecotrack.data.model.ResidentDashboardResponse
import edp.app.ecotrack.data.repository.ResidentDashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResidentDashboardViewModel : ViewModel() {

    private val repository = ResidentDashboardRepository()

    private val _dashboard = MutableStateFlow<ResidentDashboardResponse?>(null)
    val dashboard: StateFlow<ResidentDashboardResponse?> =
        _dashboard.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadDashboard(accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _dashboard.value = repository.getDashboard(accessToken)
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.message ?: "Unable to load resident dashboard."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
