import React, { useState, useEffect } from 'react';
import { FaTimes, FaBars, FaGlobe, FaHome, FaQrcode, FaPlane, FaShieldAlt, FaQuestionCircle, FaHeadset, FaLock, FaEdit, FaTrash, FaSearch, FaKey, FaFileAlt } from 'react-icons/fa';
import CryptoJS from 'crypto-js';

const SECRET_KEY = "uv-airlines-security-key"; // 🚀 실제 서비스라면 환경변수로 관리해야 해!
// 🚀 오늘 날짜를 "2026-04-26" 형식으로 추출
const today = new Date().toISOString().split('T')[0];

const Home = ({ username, onLogout }) => {
  // --- [상태 및 데이터 정의] ---
  const isAdmin = username.toLowerCase() === 'admin';
  const [currentSlide, setCurrentSlide] = useState(0);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [activeTab, setActiveTab] = useState("예약");
  const [isSearching, setIsSearching] = useState(false);
  const [isSearchDone, setIsSearchDone] = useState(false);
  const [searchResults, setSearchResults] = useState([]);

  // 🚀 예약 프로세스 관련 상태 (복구 완료)
  const [bookingStep, setBookingStep] = useState('list'); // 'list' -> 'seat' -> 'payment' -> 'complete'
  const [selectedFlight, setSelectedFlight] = useState(null);
  const [selectedSeat, setSelectedSeat] = useState(null);

  const [isChatOpen, setIsChatOpen] = useState(false);
  const [chatInput, setChatInput] = useState("");
  const [chatLog, setChatLog] = useState([
    { type: 'bot', text: "안녕하세요! UV AIRLINES 고객 중심 상담 챗봇입니다. 무엇을 도와드릴까요?" }
  ]);
  const [bookingData, setBookingData] = useState({ origin: "ICN", destination: "", departureDate: "" });

  // 🚀 게시판 관련 상태 (로컬 스토리지 로직 반영)
  const [posts, setPosts] = useState(() => {
    const saved = localStorage.getItem('uv_posts');
    return saved ? JSON.parse(saved) : [
      { id: 1, title: "항공 보안 가이드라인 (비밀글)", author: "Admin", date: "2026-04-24", isNotice: true, content: "U2FsdGVkX1+vG...", password: "1234", isEncrypted: true, fileName: "" },
    ];
  });
  const [isInquiryOpen, setIsInquiryOpen] = useState(false);
  const [viewMode, setViewMode] = useState('list');
  const [currentPost, setCurrentPost] = useState({ id: null, title: '', content: '', password: '', fileName: '' });
  const [selectedPost, setSelectedPost] = useState(null);

  // 🚀 FAQ 관련 상태 및 데이터
  const [isFaqOpen, setIsFaqOpen] = useState(false);
  const [openFaqIndex, setOpenFaqIndex] = useState(null);

  const faqData = [
    { q: "비밀번호를 분실했습니다. 어떻게 하나요?", a: "보안 정책상 비밀번호는 암호화되어 관리자도 알 수 없습니다. 게시글 작성 시 설정한 비밀번호를 반드시 기억해 주세요." },
    { q: "파일 업로드 용량 제한이 있나요?", a: "현재 최대 10MB까지 PDF, JPG, PNG 파일 업로드가 가능합니다." },
    { q: "취항지 노선은 언제 추가되나요?", a: "UV AIRLINES는 현재 SFO, CDG 노선을 운영 중이며, 추가 취항지를 검토 중입니다." }
  ];

  const menuData = {
    "예약": [{ title: "항공권", links: ["항공권 예매", "가격으로 예매"] }, { title: "정보", links: ["운임 안내", "온라인 결제"] }],
    "예약 조회": [{ title: "조회", links: ["예약 상세 조회", "비회원 예약 조회"] }],
    "탑승": [{ title: "체크인", links: ["체크인 안내"] }],
    "여행준비": [{ title: "수하물", links: ["무료 수하물", "운송 제한 물품"] }],
    "고객지원": [{ title: "게시판", links: ["통합 서식 자료실", "1:1 문의"] }],
    "이벤트": [{ title: "이벤트", links: ["진행 중 이벤트"] }]
  };

  const slides = [
    { img: "https://images.unsplash.com/photo-1529156069898-49953e39b3ac?auto=format&fit=crop&w=1920&q=80", text: "Beyond \n The \n UV vision." },
    { img: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1920&q=80", text: "Discover \n Your \n Blue Ocean." }
  ];

  const travelCards = [
    { title: "SFO", desc: "금문교의 낭만, 샌프란시스코", img: "https://images.unsplash.com/photo-1501594907352-04cda38ebc29?auto=format&fit=crop&w=800&q=80" },
    { title: "CDG", desc: "예술의 거리, 파리", img: "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=800&q=80" }
  ];

  const flightDatabase = [
    { id: 'UV201', to: 'SFO', time: '10:30', price: '842,000', type: 'Direct' },
    { id: 'UV301', to: 'CDG', time: '13:50', price: '1,100,000', type: 'Direct' },
    { id: 'UV401', to: 'JFK', time: '21:10', price: '1,250,000', type: 'Direct' },
    { id: 'UV101', to: 'NRT', time: '09:00', price: '320,000', type: 'Direct' },
    { id: 'UV501', to: 'LHR', time: '12:20', price: '1,180,000', type: 'Direct' },
    { id: 'UV601', to: 'SIN', time: '18:40', price: '550,000', type: 'Direct' },
  ];

  // --- [로직 핸들러] ---

  // 🛡️ [추가] 데이터 무결성 및 가용성 유지: posts가 변할 때마다 로컬 스토리지 저장
  useEffect(() => {
    localStorage.setItem('uv_posts', JSON.stringify(posts));
  }, [posts]);

  useEffect(() => {
    const timer = setInterval(() => setCurrentSlide(prev => (prev + 1) % slides.length), 5000);
    return () => clearInterval(timer);
  }, [slides.length]);

  const handleFinalPayment = () => {
    // 🛡️ [보안 강화] 데이터 무결성 검증 (Anti-Tampering)
    const originalFlight = flightDatabase.find(f => f.id === selectedFlight.id);
    if (!originalFlight || originalFlight.price !== selectedFlight.price) {
      alert("⚠️ 보안 경고: 결제 정보가 일치하지 않습니다. 데이터 변조가 감지되었습니다.");
      setBookingStep('list');
      return;
    }
    setBookingStep('complete');
  };

  const handleSearch = () => {
    if (!bookingData.destination) return alert("목적지를 선택해주세요!");
    setIsSearching(true);
    setTimeout(() => {
      const destCode = bookingData.destination.split(' ')[1];
      setSearchResults(flightDatabase.filter(f => f.to === destCode));
      setIsSearching(false); setIsSearchDone(true);
      setBookingStep('list');
    }, 1500);
  };

  const decryptData = (ciphertext) => {
    try {
      const bytes = CryptoJS.AES.decrypt(ciphertext, SECRET_KEY);
      const originalText = bytes.toString(CryptoJS.enc.Utf8);
      return originalText || "데이터 손상";
    } catch (err) {
      return "복호화 실패";
    }
  };

  const handleSavePost = (e) => {
    e.preventDefault();
    if (!currentPost.password) return alert("비밀번호를 설정해주세요");
    const encryptedContent = CryptoJS.AES.encrypt(currentPost.content, SECRET_KEY).toString();
    const encryptedPassword = CryptoJS.AES.encrypt(currentPost.password, SECRET_KEY).toString();
    const postToSave = { ...currentPost, content: encryptedContent, password: encryptedPassword, isEncrypted: true };
    if (currentPost.id) {
      setPosts(posts.map(p => p.id === currentPost.id ? { ...postToSave, date: new Date().toISOString().split('T')[0] } : p));
      alert("데이터가 암호화되어 안전하게 수정되었습니다.");
    } else {
      const newPost = { ...postToSave, id: Date.now(), author: username, date: new Date().toISOString().split('T')[0], isNotice: isAdmin };
      setPosts([newPost, ...posts]);
      alert("데이터가 암호화되어 안전하게 저장되었습니다.");
    }
    setViewMode('list');
    setCurrentPost({ id: null, title: '', content: '', password: '', fileName: '' });
  };

  const handleDeletePost = (id) => {
    const targetPost = posts.find(p => p.id === id);
    if (!isAdmin && targetPost.author !== username) {
      alert("⚠️ 권한 오류: 본인의 게시글만 삭제할 수 있습니다.");
      return;
    }
    if (window.confirm("정말 삭제하시겠습니까?")) {
      setPosts(posts.filter(p => p.id !== id));
      setViewMode('list');
      alert("문의글이 정상적으로 삭제되었습니다.");
    }
  };

  const handleEditPost = (post) => {
    const startEditing = (targetPost) => {
      const decryptedPost = {
        ...targetPost,
        content: targetPost.isEncrypted ? decryptData(targetPost.content) : targetPost.content,
        password: targetPost.isEncrypted ? decryptData(targetPost.password) : targetPost.password
      };
      setCurrentPost(decryptedPost);
      setViewMode('write');
    };
    if (isAdmin) { startEditing(post); return; }
    const pw = prompt("글을 수정하려면 비밀번호를 입력하세요:");
    const savedPassword = post.isEncrypted ? decryptData(post.password) : post.password;
    if (pw === savedPassword) { startEditing(post); } else { alert("비밀번호가 틀렸습니다."); }
  };

  const handleViewPost = (post) => {
    const showPost = (targetPost) => {
      const decryptedPost = { ...targetPost, content: targetPost.isEncrypted ? decryptData(targetPost.content) : targetPost.content };
      setSelectedPost(decryptedPost);
      setViewMode('view');
    };
    if (isAdmin) { showPost(post); return; }
    if (post.password) {
      const pw = prompt("이 글은 암호화되어 보호 중입니다. 비밀번호를 입력하세요:");
      const savedPassword = post.isEncrypted ? decryptData(post.password) : post.password;
      if (pw === savedPassword) { showPost(post); } else { alert("비밀번호가 틀렸습니다!"); }
    } else { showPost(post); }
  };

  const handleChatSubmit = (e) => {
    e.preventDefault();
    if (!chatInput.trim()) return;
    setChatLog(prev => [...prev, { type: 'user', text: chatInput }, { type: 'bot', text: "문의하신 내용은 보안 세션을 통해 접수되었습니다. 곧 상담원이 연결됩니다." }]);
    setChatInput("");
  };

  return (
    <div className={`min-h-screen bg-white font-sans text-slate-900 ${isMenuOpen || isInquiryOpen || isFaqOpen ? 'overflow-hidden' : ''}`}>

      {/* [1. 네비게이션] */}
      <nav className="fixed top-0 w-full z-[100] bg-white/95 backdrop-blur-sm border-b border-slate-200 flex justify-between items-center px-6 md:px-12 py-4">
        <h1 className="text-2xl font-black text-[#00205B] cursor-pointer" onClick={() => window.location.reload()}>UV AIRLINES</h1>
        <div className="flex items-center gap-4">
          <span className="text-xs font-bold text-[#00205B] hidden sm:inline">{username}님</span>
          <button onClick={() => setIsMenuOpen(true)} className="text-[#00205B] text-2xl p-1"><FaBars /></button>
        </div>
      </nav>

      {/* [2. 메뉴 오버레이] */}
      <div className={`fixed inset-0 z-[200] bg-white transition-all duration-500 ease-in-out ${isMenuOpen ? 'translate-y-0 opacity-100' : '-translate-y-full opacity-0'}`}>
        <div className="flex justify-between items-center px-6 md:px-12 py-6 border-b border-slate-100">
          <FaHome className="text-2xl cursor-pointer text-slate-800" onClick={() => setIsMenuOpen(false)} />
          <FaTimes className="text-3xl cursor-pointer text-slate-800" onClick={() => setIsMenuOpen(false)} />
        </div>
        <div className="flex w-full border-b border-slate-100 font-bold">
          <button className="flex-1 py-6 hover:bg-slate-50 transition-colors text-slate-800 border-r border-slate-100 uppercase">회원가입</button>
          <button onClick={onLogout} className="flex-1 py-6 bg-[#00205B] text-white transition-colors hover:bg-black uppercase">로그아웃</button>
        </div>
        <div className="px-6 md:px-12 border-b border-slate-100 flex gap-8 overflow-x-auto no-scrollbar font-black text-left">
          {Object.keys(menuData).map((tab) => (
            <button key={tab} onClick={() => setActiveTab(tab)} className={`py-5 text-sm md:text-base relative whitespace-nowrap transition-colors ${activeTab === tab ? 'text-[#00205B]' : 'text-slate-400 hover:text-slate-800'}`}>
              {tab}{activeTab === tab && <div className="absolute bottom-0 left-0 w-full h-1 bg-[#00205B]"></div>}
            </button>
          ))}
        </div>
        <div className="max-w-4xl mx-auto px-6 py-10 h-[calc(100vh-280px)] overflow-y-auto flex flex-col items-start gap-y-12 text-left font-bold animate-fade-in">
          {menuData[activeTab]?.map((section, idx) => (
            <div key={idx} className="w-full">
              {section.title && <h4 className="text-[11px] font-black text-slate-400 uppercase tracking-[0.2em] mb-5">{section.title}</h4>}
              <ul className="flex flex-col gap-y-4">
                {section.links.map((link, linkIdx) => (
                  <li key={linkIdx} className="text-base md:text-lg text-slate-800 hover:text-[#00205B] cursor-pointer transition-colors" onClick={() => { if (link === "통합 서식 자료실") { setIsInquiryOpen(true); setIsMenuOpen(false); } }}>
                    {link}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </div>

      {/* [3. 메인 콘텐츠] */}
      {!(isSearchDone || isInquiryOpen || isFaqOpen) ? (
        <div className="pt-20">
          <section className="relative h-[80vh] w-full bg-slate-900 overflow-hidden text-left">
            {slides.map((slide, index) => (
              <div key={index} className={`absolute inset-0 transition-opacity duration-1000 ${index === currentSlide ? 'opacity-100' : 'opacity-0'}`}>
                <img src={slide.img} alt="slide" className="w-full h-full object-cover opacity-60" />
                <div className="absolute bottom-32 left-10 md:left-24 z-20">
                  <h2 className="text-6xl md:text-8xl font-black text-white whitespace-pre-line leading-[0.85] tracking-tighter uppercase mb-8">{slide.text}</h2>
                  <div className="flex gap-3 mt-4">
                    {slides.map((_, i) => <div key={i} onClick={() => setCurrentSlide(i)} className={`h-1.5 transition-all duration-300 rounded-full ${i === currentSlide ? 'w-12 bg-[#FFD100]' : 'w-4 bg-white/40'}`}></div>)}
                  </div>
                </div>
              </div>
            ))}
          </section>

          <div className="max-w-6xl mx-auto -mt-20 relative z-20 bg-white border-2 border-[#00205B] shadow-[10px_10px_0px_0px_rgba(0,32,91,1)] grid grid-cols-1 md:grid-cols-4 divide-y md:divide-y-0 md:divide-x-2 divide-[#00205B]">
            <div className="p-6 text-left font-bold"><p className="text-[10px] text-slate-400 uppercase mb-1">From</p><h3>인천 ICN</h3></div>
            <div className="p-6 text-left font-bold">
              <p className="text-[10px] text-slate-400 uppercase mb-1">To</p>
              <select className="w-full outline-none bg-transparent cursor-pointer" onChange={(e) => setBookingData({ ...bookingData, destination: e.target.value })}>
                <option value="">어디로 떠나시나요?</option>
                <option value="샌프란시스코 SFO">샌프란시스코 SFO (미국)</option>
                <option value="파리 CDG">파리 CDG (프랑스)</option>
                <option value="뉴욕 JFK">뉴욕 JFK (미국)</option>
                <option value="도쿄 NRT">도쿄 NRT (일본)</option>
                <option value="런던 LHR">런던 LHR (영국)</option>
                <option value="싱가포르 SIN">싱가포르 SIN (싱가포르)</option>
              </select>
            </div>
            <div className="p-6 text-left font-bold">
              <p className="text-[10px] text-slate-400 uppercase mb-1">Date</p>
              <input type="date" className="w-full outline-none bg-transparent" min={today} onChange={(e) => setBookingData({ ...bookingData, departureDate: e.target.value })} />
            </div>
            <button onClick={handleSearch} className="bg-[#00205B] text-white font-black hover:bg-[#FFD100] transition-all uppercase tracking-widest">Search</button>
          </div>

          <div className="max-w-6xl mx-auto py-24 px-6">
            <h4 className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-10 text-left">Travel Destinations</h4>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-24">
              {travelCards.map((card, idx) => (
                <div key={idx} className="group border-2 border-slate-100 overflow-hidden text-left hover:border-[#00205B] transition-all cursor-pointer">
                  <div className="h-64 overflow-hidden"><img src={card.img} alt="card" className="w-full h-full object-cover grayscale group-hover:grayscale-0 transition-all duration-700" /></div>
                  <div className="p-8"><p className="text-[10px] font-black text-[#00205B] uppercase mb-1">{card.title}</p><h5 className="text-xl font-black">{card.desc}</h5></div>
                </div>
              ))}
            </div>

            <h4 className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-10 text-left">Customer Support</h4>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
              <div onClick={() => { setIsInquiryOpen(true); setViewMode('list'); }} className="bg-[#00205B] text-white p-10 flex justify-between items-center group cursor-pointer border-2 border-[#00205B] hover:bg-[#FFD100] hover:text-[#00205B] transition-all">
                <div><h5 className="text-2xl font-black mb-1 italic">1:1 BOARD</h5><p className="text-[10px] font-bold opacity-70 uppercase tracking-widest">Confidential Inquiry</p></div>
                <FaLock className="text-2xl text-slate-400 group-hover:text-[#00205B]" />
              </div>
              <div onClick={() => setIsFaqOpen(true)} className="border-4 border-[#00205B] p-10 flex justify-between items-center group cursor-pointer hover:bg-slate-50 transition-all text-left">
                <div><h5 className="text-2xl font-black mb-1 text-[#00205B]">FAQ</h5><p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">Help Center</p></div>
                <FaQuestionCircle className="text-2xl text-slate-300" />
              </div>
            </div>
          </div>
          <footer className="p-20 bg-slate-50 border-t border-slate-200 text-center text-[10px] font-black text-slate-300 uppercase tracking-[0.3em]">© 2026 UV Airlines. Project for Cybersecurity Engineering</footer>
        </div>
      ) : isSearchDone && !isInquiryOpen && !isFaqOpen ? (
        <div className="pt-32 max-w-4xl mx-auto px-6 mb-40 text-left font-bold animate-fade-in">

          {bookingStep === 'list' && (
            <>
              <button onClick={() => setIsSearchDone(false)} className="mb-6 text-sm text-slate-400 underline">← 다시 검색하기</button>
              <h2 className="text-3xl font-black text-[#00205B] mb-8 uppercase italic border-b-4 border-[#FFD100] inline-block">Available Flights</h2>
              {searchResults.map(f => (
                <div key={f.id} onClick={() => { setSelectedFlight(f); setBookingStep('seat'); }} className="bg-white border-2 border-[#00205B] p-8 flex justify-between items-center shadow-[6px_6px_0px_0px_rgba(0,32,91,1)] mb-4 cursor-pointer hover:translate-x-2 transition-all">
                  <div className="flex gap-12 items-center text-[#00205B] font-black">
                    <div><p className="text-3xl">{f.time}</p><p className="text-xs text-slate-400 uppercase">ICN</p></div>
                    <div className="text-2xl">✈️</div>
                    <div><p className="text-3xl">{f.time}</p><p className="text-xs text-slate-400 uppercase">{f.to}</p></div>
                  </div>
                  <div className="text-right"><p className="text-3xl font-black text-[#00205B]">KRW {f.price}</p></div>
                </div>
              ))}
            </>
          )}

          {bookingStep === 'seat' && (
            <div className="animate-fade-in">
              <button onClick={() => setBookingStep('list')} className="mb-6 text-sm text-slate-400 underline">← 항공편 목록으로</button>
              <h2 className="text-3xl font-black text-[#00205B] mb-8 uppercase italic text-center">Select Your Seat (Boeing 3-3-3)</h2>
              <div className="bg-white border-2 border-[#00205B] p-10 shadow-[8px_8px_0px_0px_rgba(0,32,91,1)]">
                <div className="flex flex-col gap-4 mb-10 max-w-2xl mx-auto overflow-x-auto pb-4">
                  {[...Array(5)].map((_, rowIndex) => {
                    const rowNum = rowIndex + 1;
                    return (
                      <div key={rowNum} className="flex items-center justify-center gap-2">
                        <div className="flex gap-2">
                          {['A', 'B', 'C'].map(col => {
                            const seatId = `${rowNum}${col}`;
                            return (
                              <div key={seatId} onClick={() => setSelectedSeat(seatId)} className={`w-10 h-10 md:w-12 md:h-12 border-2 flex items-center justify-center cursor-pointer font-black text-xs transition-all ${selectedSeat === seatId ? 'bg-[#FFD100] border-[#00205B]' : 'bg-slate-50 border-slate-200 hover:border-[#00205B]'}`}>{seatId}</div>
                            );
                          })}
                        </div>
                        <div className="w-4 md:w-8"></div>
                        <div className="flex gap-2">
                          {['D', 'E', 'F'].map(col => {
                            const seatId = `${rowNum}${col}`;
                            return (
                              <div key={seatId} onClick={() => setSelectedSeat(seatId)} className={`w-10 h-10 md:w-12 md:h-12 border-2 flex items-center justify-center cursor-pointer font-black text-xs transition-all ${selectedSeat === seatId ? 'bg-[#FFD100] border-[#00205B]' : 'bg-slate-50 border-slate-200 hover:border-[#00205B]'}`}>{seatId}</div>
                            );
                          })}
                        </div>
                        <div className="w-4 md:w-8"></div>
                        <div className="flex gap-2">
                          {['G', 'H', 'J'].map(col => {
                            const seatId = `${rowNum}${col}`;
                            return (
                              <div key={seatId} onClick={() => setSelectedSeat(seatId)} className={`w-10 h-10 md:w-12 md:h-12 border-2 flex items-center justify-center cursor-pointer font-black text-xs transition-all ${selectedSeat === seatId ? 'bg-[#FFD100] border-[#00205B]' : 'bg-slate-50 border-slate-200 hover:border-[#00205B]'}`}>{seatId}</div>
                            );
                          })}
                        </div>
                      </div>
                    );
                  })}
                </div>
                <button disabled={!selectedSeat} onClick={() => setBookingStep('payment')} className={`w-full py-5 font-black uppercase tracking-widest ${selectedSeat ? 'bg-[#00205B] text-white hover:bg-black' : 'bg-slate-100 text-slate-300'}`}>Confirm Seat {selectedSeat}</button>
              </div>
            </div>
          )}

          {bookingStep === 'payment' && (
            <div className="animate-fade-in">
              <button onClick={() => setBookingStep('seat')} className="mb-6 text-sm text-slate-400 underline">← 좌석 재선택</button>
              <h2 className="text-3xl font-black text-[#00205B] mb-8 uppercase italic">Secure Payment</h2>
              <div className="bg-white border-2 border-[#00205B] p-10 shadow-[8px_8px_0px_0px_rgba(0,32,91,1)] space-y-6">
                <div className="flex justify-between border-b-2 border-slate-100 pb-4"><span className="text-slate-400 font-bold uppercase text-[10px]">Selected Flight</span><span className="text-[#00205B] font-black">{selectedFlight?.id} / Seat {selectedSeat}</span></div>
                <div className="flex justify-between border-b-2 border-slate-100 pb-4"><span className="text-slate-400 font-bold uppercase text-[10px]">Total Amount</span><span className="text-2xl font-black text-[#00205B]">KRW {selectedFlight?.price}</span></div>
                <div className="p-4 bg-slate-50 border-l-4 border-[#FFD100] text-xs font-bold text-slate-500 italic">🛡️ 본 결제 시스템은 SSL/TLS 1.3 암호화 프로토콜에 의해 보호되고 있습니다.</div>
                <button onClick={handleFinalPayment} className="w-full bg-[#00205B] text-white py-5 font-black uppercase tracking-widest hover:bg-[#FFD100] hover:text-[#00205B] transition-all">Pay Now</button>
              </div>
            </div>
          )}

          {bookingStep === 'complete' && (
            <div className="animate-fade-in text-center py-20 bg-white border-2 border-[#00205B] shadow-[10px_10px_0px_0px_rgba(0,32,91,1)]">
              <div className="text-6xl mb-6">✅</div>
              <h2 className="text-4xl font-black text-[#00205B] uppercase italic mb-2">Booking Confirmed!</h2>
              <div className="max-w-xs mx-auto border-2 border-dashed border-slate-200 p-6 mb-10 text-left font-mono text-xs">
                <p>PASSENGER: {username}</p>
                <p>FLIGHT: {selectedFlight?.id}</p>
                <p>FROM/TO: ICN → {selectedFlight?.to}</p>
                <p>SEAT: {selectedSeat}</p>
                <p className="mt-4 text-[#00205B]">TICKET NO: UV-{Math.floor(Math.random() * 1000000)}</p>
              </div>
              <button onClick={() => { setIsSearchDone(false); setBookingStep('list'); }} className="bg-[#00205B] text-white px-10 py-4 font-black uppercase tracking-widest hover:bg-black transition-all">Go Home</button>
            </div>
          )}
        </div>
      ) : isFaqOpen ? (
        <div className="pt-24 min-h-screen bg-slate-50 text-left animate-fade-in fixed inset-0 z-[150] overflow-y-auto">
          <div className="max-w-4xl mx-auto px-6 pb-20">
            <div className="flex justify-between items-center mb-12 border-b-4 border-[#00205B] pb-6">
              <h2 className="text-3xl font-black text-[#00205B] uppercase italic font-black">Frequently Asked Q&A</h2>
              <FaTimes className="text-3xl cursor-pointer text-slate-300 hover:text-[#00205B]" onClick={() => setIsFaqOpen(false)} />
            </div>
            <div className="space-y-4">
              {faqData.map((item, idx) => (
                <div key={idx} className="bg-white border-2 border-slate-100 hover:border-[#00205B] transition-all">
                  <button onClick={() => setOpenFaqIndex(openFaqIndex === idx ? null : idx)} className="w-full p-6 flex justify-between items-center text-left">
                    <span className="font-black text-slate-700"><span className="text-[#FFD100] mr-3 italic text-xl font-black">Q.</span>{item.q}</span>
                    <span className={`transition-transform duration-300 ${openFaqIndex === idx ? 'rotate-180' : ''}`}>▼</span>
                  </button>
                  {openFaqIndex === idx && (
                    <div className="p-6 pt-0 bg-slate-50 animate-fade-in text-sm font-bold text-slate-500">
                      <div className="border-t border-slate-200 pt-4"><span className="text-[#00205B] mr-3 italic text-xl font-black">A.</span>{item.a}</div>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>
      ) : (
        <div className="pt-24 min-h-screen bg-slate-50 text-left fixed inset-0 z-[150] overflow-y-auto">
          <div className="max-w-5xl mx-auto px-6 pb-20">
            <div className="flex justify-between items-center mb-8 border-b-4 border-[#00205B] pb-6">
              <h2 className="text-3xl font-black text-[#00205B] uppercase italic">문의하기</h2>
              <FaTimes className="text-3xl cursor-pointer text-slate-300 hover:text-red-500" onClick={() => setIsInquiryOpen(false)} />
            </div>
            {viewMode === 'list' ? (
              <div className="bg-white border border-slate-200 shadow-sm overflow-hidden font-bold">
                <table className="w-full text-sm text-left">
                  <thead className="bg-slate-50 border-b-2 font-black text-slate-600">
                    <tr><th className="py-4 w-20 text-center">번호</th><th className="px-6">제목</th><th className="w-32 text-center">작성자</th><th className="w-32 text-center">날짜</th></tr>
                  </thead>
                  <tbody className="text-slate-700 font-bold">
                    {posts.map((post, idx) => (
                      <tr key={post.id} className="border-b hover:bg-slate-50 cursor-pointer" onClick={() => handleViewPost(post)}>
                        <td className="py-5 text-center text-slate-400 font-mono text-xs">{posts.length - idx}</td>
                        <td className="px-6 flex items-center gap-2">{post.password && <FaLock className="text-slate-200 text-[10px]" />} {post.title}</td>
                        <td className="text-center text-xs">{post.author}</td>
                        <td className="text-center text-slate-400 font-mono text-[10px]">{post.date}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                <div className="p-6 flex justify-end"><button onClick={() => setViewMode('write')} className="bg-[#00205B] text-[#FFD100] px-10 py-3 font-black uppercase">글쓰기</button></div>
              </div>
            ) : viewMode === 'write' ? (
              <div className="bg-white border-2 border-[#00205B] p-10 font-bold">
                <form onSubmit={handleSavePost} className="space-y-6">
                  <input className="w-full p-4 border font-black outline-none focus:border-[#00205B]" placeholder="제목" value={currentPost.title} onChange={(e) => setCurrentPost({ ...currentPost, title: e.target.value })} />
                  <textarea className="w-full p-4 border font-black h-64 outline-none resize-none focus:border-[#00205B]" placeholder="내용" value={currentPost.content} onChange={(e) => setCurrentPost({ ...currentPost, content: e.target.value })} />

                  {/* 🚀 복구된 파일 드래그 & 업로드 섹션 */}
                  <div className="relative group">
                    <label className="text-[10px] font-black text-slate-400 uppercase tracking-widest mb-2 block text-left">Attachment</label>
                    <div className="border-2 border-dashed border-slate-200 p-8 rounded-lg flex flex-col items-center justify-center bg-slate-50 hover:border-[#00205B] transition-all cursor-pointer relative"
                      onDragOver={(e) => e.preventDefault()}
                      onDrop={(e) => {
                        e.preventDefault();
                        const file = e.dataTransfer.files[0];
                        if (file) setCurrentPost({ ...currentPost, fileName: file.name });
                      }}>
                      <input type="file" className="absolute inset-0 opacity-0 cursor-pointer" onChange={(e) => {
                        const file = e.target.files[0];
                        if (file) setCurrentPost({ ...currentPost, fileName: file.name });
                      }} />
                      <FaFileAlt className="text-2xl text-slate-300 mb-2" />
                      <p className="text-xs font-bold text-slate-500">{currentPost.fileName ? `Selected: ${currentPost.fileName}` : "파일을 선택하거나 여기로 끌어다 놓으세요"}</p>
                    </div>
                  </div>

                  <div className="relative">
                    <FaKey className="absolute left-4 top-5 text-slate-300" />
                    <input type="password" virtualkeyboardpolicy="manual" className="w-full p-4 pl-12 border font-black outline-none focus:border-[#00205B]" placeholder="비밀번호 설정" value={currentPost.password} onChange={(e) => setCurrentPost({ ...currentPost, password: e.target.value })} />
                  </div>

                  <div className="flex gap-4">
                    <button type="submit" className="flex-1 bg-[#00205B] text-white py-4 font-black">SAVE POST</button>
                    <button type="button" onClick={() => setViewMode('list')} className="flex-1 border-2 font-black py-4 uppercase">Cancel</button>
                  </div>
                </form>
              </div>
            ) : (
              <div className="bg-white border-2 border-[#00205B] p-10 relative min-h-[500px] font-bold">
                <div className="flex justify-between items-center border-b-2 pb-4 mb-8">
                  <h3 className="text-3xl font-black text-[#00205B] uppercase italic">{selectedPost?.title}</h3>
                  <button onClick={() => setViewMode('list')} className="text-sm font-black text-slate-400 underline uppercase">닫기</button>
                </div>
                <div className="text-slate-700 leading-relaxed font-bold text-lg mb-8 whitespace-pre-wrap">{selectedPost?.content}</div>
                {selectedPost?.fileName && (
                  <div className="mb-10 p-4 bg-slate-50 border-l-4 border-[#00205B] flex items-center gap-3">
                    <FaFileAlt className="text-[#00205B]" />
                    <span className="text-sm font-bold text-slate-600">첨부파일: {selectedPost.fileName}</span>
                    <button className="ml-auto text-[10px] font-black uppercase text-[#00205B] hover:underline" onClick={() => alert("보안 스캔 후 다운로드를 시작합니다.")}>Download</button>
                  </div>
                )}
                <div className="absolute bottom-10 left-10 right-10 flex justify-between items-center border-t pt-6">
                  <span className="text-xs font-black text-slate-300 uppercase font-mono font-black">Author: {selectedPost?.author}</span>
                  <div className="flex gap-6">
                    {(isAdmin || selectedPost?.author === username) && (
                      <>
                        <button onClick={() => handleEditPost(selectedPost)} className="text-[#00205B] font-black uppercase flex items-center gap-2 hover:underline"><FaEdit size={14} /> Edit</button>
                        <button onClick={() => handleDeletePost(selectedPost.id)} className="text-red-500 font-black uppercase flex items-center gap-2 hover:underline"><FaTrash size={14} /> Delete</button>
                      </>
                    )}
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      )}

      {/* 🚀 챗봇 UI */}
      {isChatOpen && (
        <div className="fixed bottom-28 right-10 w-80 md:w-96 h-[500px] bg-white border-2 border-[#00205B] shadow-[10px_10px_0px_0px_rgba(0,32,91,1)] z-[400] flex flex-col animate-slide-up text-left">
          <div className="bg-[#00205B] text-white p-4 flex justify-between items-center">
            <div className="flex items-center gap-2 font-black italic text-sm"><FaShieldAlt className="text-[#FFD100]" /> AI 챗봇 LUV</div>
            <FaTimes className="cursor-pointer hover:text-[#FFD100]" onClick={() => setIsChatOpen(false)} />
          </div>
          <div className="flex-1 overflow-y-auto p-4 flex flex-col gap-3 bg-slate-50">
            {chatLog.map((chat, idx) => (
              <div key={idx} className={`flex ${chat.type === 'user' ? 'justify-end' : 'justify-start'}`}>
                <div className={`max-w-[80%] p-3 text-xs font-bold shadow-sm ${chat.type === 'user' ? 'bg-[#FFD100] text-[#00205B] rounded-l-lg rounded-tr-lg' : 'bg-white text-slate-800 border border-slate-100 rounded-r-lg rounded-tl-lg'}`}>
                  {chat.text}
                </div>
              </div>
            ))}
          </div>
          <form onSubmit={handleChatSubmit} className="p-4 border-t flex gap-2 bg-white">
            <input type="text" value={chatInput} onChange={(e) => setChatInput(e.target.value)} placeholder="메시지를 입력하세요..." className="flex-1 text-xs font-bold outline-none border-b-2 border-transparent focus:border-[#00205B] pb-1" />
            <button type="submit" className="text-[#00205B] font-black text-xs uppercase hover:text-[#FFD100] transition-colors">전송</button>
          </form>
        </div>
      )}
      <button onClick={() => setIsChatOpen(!isChatOpen)} className="fixed bottom-10 right-10 w-16 h-16 bg-[#00205B] text-[#FFD100] rounded-full flex items-center justify-center text-3xl shadow-xl z-[300] transition-all hover:scale-110 active:scale-95"><FaShieldAlt /></button>
    </div>
  );
};

export default Home;