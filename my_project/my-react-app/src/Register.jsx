import React, { useState } from 'react';
import axios from 'axios';
import { FaUserPlus, FaEnvelope, FaLock, FaCheckCircle, FaArrowLeft } from 'react-icons/fa';

const Register = ({ onBackToLogin }) => {
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });

    const handleChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        if (user.password !== user.confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }
        try {
            const response = await axios.post('http://localhost:5000/api/register', {
                username: user.username,
                email: user.email,
                password: user.password
            });
            if (response.data.success) {
                alert("UV AIRLINES 가입을 환영합니다!");
                onBackToLogin();
            }
        } catch (error) {
            alert(error.response?.data?.message || "회원가입 오류 발생");
        }
    };

    return (
        <div className="min-h-screen bg-[#F8F9FA] flex flex-col items-center font-sans text-slate-900">
            {/* 상단바: 로그인 페이지와 통일 */}
            <nav className="w-full bg-white border-b border-slate-200 flex justify-between items-center px-6 md:px-12 py-4 fixed top-0 z-50">
                <div className="flex items-center gap-1 cursor-pointer" onClick={onBackToLogin}>
                    <h1 className="text-2xl font-black tracking-tighter text-[#00205B]">UV AIRLINES</h1>
                    <div className="w-2.5 h-2.5 bg-[#FFD100] mt-1.5 rounded-full"></div>
                </div>
            </nav>

            {/* 카드 섹션: 인라인 스타일 제거하고 Tailwind 적용 */}
            <div className="w-full max-w-md mt-32 px-8 py-10 bg-white border-2 border-[#00205B] shadow-[12px_12px_0px_0px_rgba(0,32,91,1)] rounded-lg">
                <h2 className="text-3xl font-black text-[#00205B] text-center mb-2 uppercase italic">회원가입</h2>
                <p className="text-[10px] font-bold text-slate-400 text-center mb-10 tracking-widest uppercase">Create your security account</p>

                <form onSubmit={handleRegister} className="space-y-6">
                    <div className="relative">
                        <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest absolute -top-2 left-4 bg-white px-2">User ID</label>
                        <input name="username" type="text" onChange={handleChange} className="w-full px-4 py-3 border-2 border-slate-100 focus:border-[#00205B] outline-none font-bold rounded" required />
                    </div>

                    <div className="relative">
                        <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest absolute -top-2 left-4 bg-white px-2">Email</label>
                        <input name="email" type="email" onChange={handleChange} className="w-full px-4 py-3 border-2 border-slate-100 focus:border-[#00205B] outline-none font-bold rounded" required />
                    </div>

                    <div className="relative">
                        <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest absolute -top-2 left-4 bg-white px-2">Password</label>
                        <input name="password" type="password" onChange={handleChange} className="w-full px-4 py-3 border-2 border-slate-100 focus:border-[#00205B] outline-none font-bold rounded" required />
                    </div>

                    <div className="relative">
                        <label className="text-[10px] font-black uppercase text-slate-400 tracking-widest absolute -top-2 left-4 bg-white px-2">Confirm Password</label>
                        <input name="confirmPassword" type="password" onChange={handleChange} className="w-full px-4 py-3 border-2 border-slate-100 focus:border-[#00205B] outline-none font-bold rounded" required />
                    </div>

                    <button type="submit" className="w-full bg-[#00205B] text-white py-4 font-black uppercase tracking-widest hover:bg-[#FFD100] hover:text-[#00205B] transition-all shadow-lg">
                        Sign Up Now
                    </button>

                    <button type="button" onClick={onBackToLogin} className="w-full text-xs font-black text-slate-400 hover:text-[#00205B] transition-colors uppercase tracking-widest pt-2">
                        ← Cancel and Back
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Register;