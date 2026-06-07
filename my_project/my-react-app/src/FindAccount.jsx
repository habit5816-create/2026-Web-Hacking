import React, { useState } from 'react';
import axios from 'axios';
import { FaSearch, FaArrowLeft, FaKey } from 'react-icons/fa';

const FindAccount = ({ mode, onBackToLogin }) => {
    const [step, setStep] = useState(mode); // 'id' 또는 'pw'
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [result, setResult] = useState(null);

    const handleFind = async (e) => {
        e.preventDefault();
        try {
            const endpoint = step === 'id' ? '/api/find-id' : '/api/find-pw';
            const payload = step === 'id' ? { email } : { username, email };

            const response = await axios.post(`http://localhost:5000${endpoint}`, payload);

            if (response.data.success) {
                setResult(response.data.message); // 예: "아이디는 gwen*** 입니다"
            }
        } catch (error) {
            alert(error.response?.data?.message || "일치하는 정보가 없습니다.");
        }
    };

    return (
        <div className="min-h-screen bg-[#F8F9FA] flex flex-col items-center font-sans text-slate-900">
            <nav className="w-full bg-white border-b border-slate-200 flex justify-between items-center px-12 py-4 fixed top-0">
                <h1 className="text-2xl font-black text-[#00205B]">UV AIRLINES</h1>
            </nav>

            <div className="w-full max-w-md mt-40 px-8 py-10 bg-white border-2 border-[#00205B] shadow-[12px_12px_0px_0px_rgba(0,32,91,1)]">
                <h2 className="text-2xl font-black text-[#00205B] mb-8 uppercase italic">
                    {step === 'id' ? 'Find Your ID' : 'Reset Password'}
                </h2>

                {!result ? (
                    <form onSubmit={handleFind} className="space-y-6">
                        {step === 'pw' && (
                            <div className="relative">
                                <label className="text-[10px] font-black uppercase text-slate-400 absolute -top-2 left-4 bg-white px-2">User ID</label>
                                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} className="w-full px-4 py-3 border-2 border-slate-100 focus:border-[#00205B] outline-none font-bold" required />
                            </div>
                        )}
                        <div className="relative">
                            <label className="text-[10px] font-black uppercase text-slate-400 absolute -top-2 left-4 bg-white px-2">Email Address</label>
                            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} className="w-full px-4 py-3 border-2 border-slate-100 focus:border-[#00205B] outline-none font-bold" required />
                        </div>
                        <button type="submit" className="w-full bg-[#00205B] text-white py-4 font-black uppercase tracking-widest hover:bg-[#FFD100] hover:text-[#00205B] transition-all">
                            {step === 'id' ? 'Find My ID' : 'Get Reset Link'}
                        </button>
                    </form>
                ) : (
                    <div className="text-center py-6">
                        <div className="bg-slate-50 p-6 border-2 border-dashed border-slate-200 mb-8">
                            <p className="text-sm font-bold text-slate-600">{result}</p>
                        </div>
                        <button onClick={onBackToLogin} className="w-full bg-[#00205B] text-white py-4 font-black">BACK TO LOGIN</button>
                    </div>
                )}

                <button onClick={onBackToLogin} className="w-full text-[10px] font-black text-slate-400 mt-6 uppercase hover:underline">Back to Login</button>
            </div>
        </div>
    );
};

export default FindAccount;