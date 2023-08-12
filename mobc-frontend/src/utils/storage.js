export const storageHelper = {
    setUserInfo(data) {
        localStorage.setItem('USER_INFO', JSON.stringify(data || {}))
    },
    getUserInfo(data) {
        return localStorage.getItem('USER_INFO') ? JSON.parse(localStorage.getItem('USER_INFO')) : null
    },
    clearCache() {
        localStorage.clear()
    }
}
