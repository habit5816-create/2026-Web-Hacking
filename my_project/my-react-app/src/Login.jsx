import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaEye, FaEyeSlash, FaExclamationCircle } from 'react-icons/fa';

const Login = ({ onLoginSuccess, onGoToRegister }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [isCapsLockOn, setIsCapsLockOn] = useState(false);

    useEffect(() => {
        const savedUsername = localStorage.getItem('savedUsername');
        if (savedUsername) {
            setUsername(savedUsername);
            setRememberMe(true);
        }
    }, []);

    const checkCapsLock = (e) => {
        if (e.getModifierState('CapsLock')) {
            setIsCapsLockOn(true);
        } else {
            setIsCapsLockOn(false);
        }
    };

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:5000/api/login', { username, password });
            if (response.data.success) {
                if (rememberMe) {
                    localStorage.setItem('savedUsername', username);
                } else {
                    localStorage.removeItem('savedUsername');
                }
                alert("로그인 성공!");
                onLoginSuccess(response.data.user, response.data.token);
            }
        } catch (error) {
            alert(error.response?.data?.message || "서버 에러가 발생했습니다.");
        }
    };

    const handlePasswordVisibilityOn = () => setShowPassword(true);
    const handlePasswordVisibilityOff = () => setShowPassword(false);

    return (
        <div className="min-h-screen bg-[#F8F9FA] flex flex-col items-center font-sans text-slate-900 overflow-x-hidden">
            <style>
                {`
                    input[type="password"]::-ms-reveal,
                    input[type="password"]::-ms-clear { display: none; }
                    input[type="password"]::-webkit-contacts-auto-fill-button,
                    input[type="password"]::-webkit-credentials-auto-fill-button { display: none !important; }
                `}
            </style>

            {/* 상단 네비게이션 로고 영역 수정 */}
            <nav className="w-full bg-white border-b border-slate-200 flex justify-between items-center px-6 md:px-12 py-4 fixed top-0 z-50">
                <div
                    className="flex items-center gap-1 cursor-pointer hover:opacity-70 transition-opacity"
                    onClick={() => window.location.href = '/'} // 🚀 로고 누르면 홈(/)으로 이동!
                >
                    <h1 className="text-2xl font-black tracking-tighter text-[#00205B]">UV AIRLINES</h1>
                    <div className="w-2.5 h-2.5 bg-[#FFD100] mt-1.5 rounded-full"></div>
                </div>
            </nav>

            <div className="w-full max-w-lg mt-32 px-6 py-10 bg-white shadow-sm border border-slate-100 md:rounded-lg mb-10">
                <h2 className="text-3xl font-black text-center mb-12 tracking-tight text-[#00205B]">로그인</h2>

                <form onSubmit={handleLogin} className="space-y-7 mb-16">
                    <div className="relative">
                        <label className="text-[11px] font-black uppercase text-slate-400 tracking-widest absolute -top-2.5 left-4 bg-white px-2 z-10">User ID</label>
                        <input
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            className="w-full px-5 py-4 border-2 border-slate-200 focus:border-[#00205B] outline-none text-base font-medium transition-colors rounded"
                            required
                        />
                    </div>

                    <div className="relative">
                        <label className="text-[11px] font-black uppercase text-slate-400 tracking-widest absolute -top-2.5 left-4 bg-white px-2 z-10">Password</label>
                        <input
                            type={showPassword ? "text" : "password"}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            onKeyUp={checkCapsLock}
                            onKeyDown={checkCapsLock}
                            className="w-full px-5 py-4 border-2 border-slate-200 focus:border-[#00205B] outline-none text-base font-medium transition-colors rounded"
                            required
                        />
                        <button
                            type="button"
                            onMouseDown={handlePasswordVisibilityOn}
                            onMouseUp={handlePasswordVisibilityOff}
                            onMouseLeave={handlePasswordVisibilityOff}
                            className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-[#00205B] p-1 focus:outline-none"
                        >
                            {showPassword ? <FaEyeSlash size={20} /> : <FaEye size={20} />}
                        </button>

                        {isCapsLockOn && (
                            <div className="absolute left-0 -bottom-8 flex items-center gap-1.5 text-red-500 animate-pulse z-10 bg-white/80 px-2 py-1">
                                <FaExclamationCircle size={14} />
                                <span className="text-[11px] font-bold">Caps Lock이 켜져 있습니다.</span>
                            </div>
                        )}
                    </div>

                    <div className="flex items-center gap-2 px-1 pt-3">
                        <input
                            type="checkbox"
                            id="remember"
                            checked={rememberMe}
                            onChange={(e) => setRememberMe(e.target.checked)}
                            className="w-4 h-4 accent-[#00205B] cursor-pointer"
                        />
                        <label htmlFor="remember" className="text-sm font-bold text-slate-500 cursor-pointer select-none">아이디 저장</label>
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-[#00205B] text-white py-5 font-black text-lg uppercase tracking-widest hover:bg-yellow-400 hover:text-[#00205B] transition-all duration-300 rounded shadow-lg shadow-blue-900/10 active:scale-[0.98]"
                    >
                        Log In
                    </button>
                </form>

                {/* --- 🚀 소셜 로그인 영역 (image_9.png 완벽 오마주) --- */}
                <div className="mt-12 text-center border-t border-slate-100 pt-12">

                    {/* 버튼 및 문구 그리드 배치 */}
                    <div className="grid grid-cols-4 gap-x-3 gap-y-2 justify-items-center">

                        {/* 네이버 (Naver) */}
                        <button type="button" className="w-[60px] h-[60px] bg-[#03C75A] rounded-full flex items-center justify-center text-white text-[28px] font-black hover:opacity-85 transition-opacity shadow-sm">
                            N
                        </button>

                        {/* 카카오 (Kakao) */}
                        <button type="button" className="w-[60px] h-[60px] bg-[#FEE500] rounded-full flex items-center justify-center text-[#3C1E1E] text-3xl hover:opacity-85 transition-opacity shadow-sm">
                            💬
                        </button>

                        {/* 구글 (Google) */}
                        <button type="button" className="w-[60px] h-[60px] bg-white border border-slate-200 rounded-full flex items-center justify-center hover:bg-slate-50 transition-all shadow-sm">
                            <img src="https://www.gstatic.com/images/branding/product/1x/gsa_512dp.png" alt="G" className="w-7 h-7" />
                        </button>

                        {/* 애플 (Apple) */}
                        <button type="button" className="w-[60px] h-[60px] bg-black rounded-full flex items-center justify-center text-white text-3xl pb-1 hover:opacity-85 transition-opacity shadow-sm">
                            
                        </button>

                        {/* 하단 설명 문구 영역 (4열 병합 안 함) */}
                        <p className="text-[10px] font-bold text-slate-500 mt-1">네이버 로그인</p>
                        <p className="text-[10px] font-bold text-slate-500 mt-1">카카오 로그인</p>
                        <p className="text-[10px] font-bold text-slate-500 mt-1">구글 로그인</p>
                        <p className="text-[10px] font-bold text-slate-500 mt-1">애플 로그인</p>
                    </div>

                    {/* 하단 유틸리티 링크 (Create Account 등) */}
                    <div className="mt-14 pt-8 border-t border-slate-50 flex flex-col items-center gap-4">
                        <button onClick={onGoToRegister} className="text-[10px] font-black uppercase tracking-[0.2em] border-2 border-[#00205B] px-8 py-3 hover:bg-[#00205B] hover:text-white transition-all duration-300">
                            Create Account
                            <div className="mt-4 flex justify-center gap-6 text-[10px] font-black uppercase text-slate-400">
                                <button type="button" onClick={() => onGoToFind('id')} className="hover:text-[#00205B]">Find ID</button>
                                <span className="text-slate-200">|</span>
                                <button type="button" onClick={() => onGoToFind('pw')} className="hover:text-[#00205B]">Find Password</button>
                            </div>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;