const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const bcrypt = require('bcryptjs');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use((req, res, next) => {
    console.log(`[${new Date().toISOString()}] Incoming Request: ${req.method} ${req.url}`);
    next();
});
app.use(express.json());

// 서버 접속 확인용 테스트 경로
app.get('/')

// MySQL Connection (데이터베이스 지정 없이 연결)
const db = mysql.createConnection({
    host: process.env.DB_HOST || 'localhost',
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASSWORD || ''
});

db.connect((err) => {
    if (err) {
        console.error('MySQL 연결 실패:', err);
    } else {
        console.log('MySQL 연결 성공!');

        const dbName = process.env.DB_NAME || 'testdb';
        db.query(`CREATE DATABASE IF NOT EXISTS ${dbName}`, (err) => {
            if (err) {
                console.error('데이터베이스 생성 실패:', err);
            } else {
                console.log(`데이터베이스 ${dbName} 확인/생성 완료`);
                db.query(`USE ${dbName}`, (err) => {
                    if (err) {
                        console.error('데이터베이스 선택 실패:', err);
                    } else {
                        const createTableQuery = `
                            CREATE TABLE IF NOT EXISTS users (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                username VARCHAR(255) NOT NULL UNIQUE,
                                email VARCHAR(255) NOT NULL UNIQUE,
                                password VARCHAR(255) NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                            )
                        `;
                        db.query(createTableQuery, (err) => {
                            if (err) console.error('테이블 생성 실패:', err);
                            else {
                                console.log('Users 테이블 확인 완료');
                                // 테스트용 데이터 삽입 (존재하지 않을 때만)
                                db.query("SELECT * FROM users WHERE username = 'test'", async (err, results) => {
                                    if (results && results.length === 0) {
                                        const hashedPassword = await bcrypt.hash('1234', 10);
                                        db.query("INSERT INTO users (username, email, password) VALUES ('test', 'test@example.com', ?)", [hashedPassword], (err) => {
                                            if (err) console.error(err);
                                            else console.log('테스트 계정(test/1234) 생성 완료');
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
});

// 회원가입 API
app.post('/api/register', async (req, res) => {
    console.log('회원가입 요청 도착:', req.body);
    const { username, email, password } = req.body;

    // 1. 모든 필드 입력 확인 (유효성 검사)
    if (!username || !email || !password) {
        return res.status(400).json({ success: false, message: '모든 필드를 입력해주세요.' });
    }

    try {
        // 2. 이메일 형식 검사
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            return res.status(400).json({ success: false, message: '유효한 이메일 형식이 아닙니다.' });
        }

        // 3. 중복 확인 (username, email) - Prepared Statement 사용
        const checkQuery = 'SELECT * FROM users WHERE username = ? OR email = ?';
        db.query(checkQuery, [username, email], async (err, results) => {
            if (err) {
                console.error('DB 조회 에러:', err);
                return res.status(500).json({ success: false, message: '서버 에러' });
            }

            if (results.length > 0) {
                // 이미 존재하는 계정인 경우
                const existingUser = results[0];
                if (existingUser.username === username) {
                    return res.status(409).json({ success: false, message: '이미 존재하는 사용자 이름입니다.' });
                }
                if (existingUser.email === email) {
                    return res.status(409).json({ success: false, message: '이미 가입된 이메일입니다.' });
                }
            }

            // 4. 비밀번호 암호화 (bcrypt 사용 - password_hash 역할)
            const hashedPassword = await bcrypt.hash(password, 10);

            // 5. DB 저장 - Prepared Statement 사용
            const insertQuery = 'INSERT INTO users (username, email, password) VALUES (?, ?, ?)';
            db.query(insertQuery, [username, email, hashedPassword], (err, insertResults) => {
                if (err) {
                    console.error('DB 저장 에러:', err);
                    return res.status(500).json({ success: false, message: '회원가입 처리 중 에러가 발생했습니다.' });
                }

                res.status(201).json({ success: true, message: '회원가입이 완료되었습니다.' });
            });
        });
    } catch (error) {
        console.error('회원가입 처리 에러:', error);
        res.status(500).json({ success: false, message: '서버 내부 에러가 발생했습니다.' });
    }
});

// 로그인 API 수정 (암호화된 비밀번호 호환)
app.post('/api/login', (req, res) => {
    console.log('로그인 요청 도착:', req.body);
    const { username, password } = req.body;

    if (!username || !password) {
        console.log('아이디/비번호 누락됨');
        return res.status(400).json({ success: false, message: '아이디와 비밀번호를 입력해주세요.' });
    }

    // 암호화된 비밀번호를 비교하기 위해 username만으로 조회
    const query = 'SELECT * FROM users WHERE username = ?';
    console.log('DB 쿼리 실행 직전:', query, [username]);
    db.query(query, [username], async (err, results) => {
        if (err) {
            console.error('DB 쿼리 에러:', err);
            return res.status(500).json({ success: false, message: '서버 에러' });
        }

        console.log('DB 쿼리 결과:', results);
        if (results.length > 0) {
            const user = results[0];
            // 입력된 비밀번호와 DB에 저장된 암호화된 비밀번호 비교
            const isMatch = await bcrypt.compare(password, user.password);

            if (isMatch) {
                res.json({ success: true, message: '로그인 성공!', user: { username: user.username } });
            } else {
                res.status(401).json({ success: false, message: '아이디 또는 비밀번호가 틀렸습니다.' });
            }
        } else {
            res.status(401).json({ success: false, message: '아이디 또는 비밀번호가 틀렸습니다.' });
        }
    });
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`서버가 http://0.0.0.0:${PORT} 에서 실행 중입니다.`);
});
