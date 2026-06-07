import { useState, useEffect } from 'react'
import './App.css'
import Login from './Login'
import Register from './Register'
import Home from './Home'

function App() {
  const [user, setUser] = useState(null)
  const [isRegistering, setIsRegistering] = useState(false)
  const [isLoading, setIsLoading] = useState(true) // 로딩 상태 추가

  // 1. 앱이 처음 켜질 때(혹은 새로고침 시) 실행
  useEffect(() => {
    const savedUser = localStorage.getItem('user')
    const savedToken = localStorage.getItem('token')

    if (savedUser && savedToken) {
      // 로컬 스토리지에 정보가 있다면 유저 상태 복구
      // (실무에서는 여기서 서버에 토큰 검증 API를 쏴야 하지만, 우선은 로컬 데이터로 복구!)
      setUser(JSON.parse(savedUser))
    }
    setIsLoading(false)
  }, [])

  // 2. 로그인 성공 시 실행되는 함수 (토큰과 유저 정보 저장)
  const handleLoginSuccess = (userData, token) => {
    localStorage.setItem('user', JSON.stringify(userData)) // 유저 정보 저장
    localStorage.setItem('token', token) // JWT 토큰 저장
    setUser(userData)
  }

  // 3. 로그아웃 함수 (저장된 정보 싹 지우기)
  const handleLogout = () => {
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    setUser(null)
  }

  // 로딩 중일 때는 아무것도 안 보여줌 (깜빡임 방지)
  if (isLoading) return <div className="bg-white min-h-screen"></div>

  // 4. 로그인 안 된 상태
  if (!user) {
    if (isRegistering) {
      return <Register onBackToLogin={() => setIsRegistering(false)} />
    }
    return (
      <Login
        onLoginSuccess={handleLoginSuccess}
        onGoToRegister={() => setIsRegistering(true)}
      />
    )
  }

  // 5. 로그인 된 상태 (에어로케이 홈 화면)
  return (
    <Home
      username={user.username}
      onLogout={handleLogout}
    />
  )
}

export default App