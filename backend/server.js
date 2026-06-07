const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken'); // 🚀 JWT 추가
const helmet = require('helmet');     // 🚀 보안 헤더 추가
const rateLimit = require('express-rate-limit'); // 🚀 무차별 대입 방지 추가
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 5000;
const JWT_SECRET = process.env.JWT_SECRET || 'AERO_K_SECRET_KEY_2026'; // 보안상 .env에 두는 것이 좋음

// --- 1. 보안 미들웨어 설정 ---

// Helmet: HTTP 헤더 설정을 통해 XSS, 클릭재킹 등 기본적인 웹 공격 방어
app.use(helmet());

// CORS 설정: 실제 서비스라면 origin을 특정 도메인으로 제한하는 것이 좋음
app.use(cors());

// Rate Limiting: 무차별 대입 공격(Brute Force) 방지를 위해 IP당 요청 횟수 제한
const limiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15분
    max: 100, // IP당 최대 100번 요청 가능
    message: { success: false, message: "너무 많은 요청이 발생했습니다. 잠시 후 다시 시도해주세요." }
});
app.use('/api/', limiter); // API 경로에만 적용

app.use(express.json());

// 로깅 미들웨어
app.use((req, res, next) => {
    console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);
    next();
});

// --- 2. MySQL 연결 및 초기화 ---

const db = mysql.createConnection({
    host: process.env.DB_HOST || 'localhost',
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASSWORD || '',
    multipleStatements: true // 초기화를 위한 다중 쿼리 허용
});

db.connect((err) => {
    if (err) {
        console.error('MySQL 연결 실패:', err);
        return;
    }
    console.log('MySQL 연결 성공!');

    const dbName = process.env.DB_NAME || 'testdb';
    db.query(`CREATE DATABASE IF NOT EXISTS ${dbName}; USE ${dbName};`, (err) => {
        if (err) return console.error('DB 초기화 에러:', err);

        const createTableQuery = `
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(255) NOT NULL UNIQUE,
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        `;
        db.query(createTableQuery, async (err) => {
            if (err) console.error('테이블 생성 실패:', err);
            else console.log('DB 및 Users 테이블 준비 완료');
        });
    });
});

// --- 3. API 경로 설정 ---

// 회원가입 API
app.post('/api/register', async (req, res) => {
    const { username, email, password } = req.body;
    try {
        const hashedPassword = await bcrypt.hash(password, 10);
        const sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        db.query(sql, [username, email, hashedPassword], (err) => {
            if (err) {
                return res.status(400).json({ success: false, message: "중복된 사용자 정보가 존재합니다." });
            }
            res.json({ success: true, message: "회원가입 성공!" });
        });
    } catch (error) {
        res.status(500).json({ success: false, message: "서버 내부 오류" });
    }
});

// 로그인 API (JWT 발급 적용)
app.post('/api/login', (req, res) => {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).json({ success: false, message: '정보를 모두 입력해주세요.' });
    }

    const query = 'SELECT * FROM users WHERE username = ?';
    db.query(query, [username], async (err, results) => {
        if (err || results.length === 0) {
            return res.status(401).json({ success: false, message: '인증에 실패했습니다.' });
        }

        const user = results[0];
        const isMatch = await bcrypt.compare(password, user.password);

        if (isMatch) {
            // 🚀 JWT 토큰 생성: 사용자 정보와 만료 시간 포함
            const token = jwt.sign(
                { id: user.id, username: user.username },
                JWT_SECRET,
                { expiresIn: '1h' } // 1시간 동안 유효
            );

            res.json({
                success: true,
                message: '로그인 성공!',
                token: token, // 🚀 클라이언트에 증명서 발송
                user: { username: user.username }
            });
        } else {
            res.status(401).json({ success: false, message: '인증에 실패했습니다.' });
        }
    });
});

// 🚀 토큰 검증 미들웨어 (보안이 필요한 경로에 사용)
const authenticateToken = (req, res, next) => {
    const authHeader = req.headers['authorization'];
    const token = authHeader && authHeader.split(' ')[1];

    if (!token) return res.status(401).json({ message: "토큰이 없습니다." });

    jwt.verify(token, JWT_SECRET, (err, user) => {
        if (err) return res.status(403).json({ message: "유효하지 않은 토큰입니다." });
        req.user = user;
        next();
    });
};

// 보호된 경로 예시 (예: 예약 정보 조회)
app.get('/api/protected', authenticateToken, (req, res) => {
    res.json({ message: "인증된 사용자만 볼 수 있는 데이터입니다.", user: req.user });
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`보안 서버 실행 중: http://localhost:${PORT}`);
});
