-- bot表
DROP TABLE IF EXISTS bot;
CREATE TABLE bot
(
    id                     SERIAL PRIMARY KEY,
    type                   VARCHAR(50) NOT NULL DEFAULT 'MAIN',
    bot_token              VARCHAR(512),
    description            VARCHAR(512),
    bot_telegram_user_name VARCHAR(50),
    created_at             TIMESTAMPTZ          DEFAULT NOW(),
    updated_at             TIMESTAMPTZ          DEFAULT NOW()
);

-- telegram使用者表
DROP TABLE IF EXISTS telegram_user;
CREATE TABLE telegram_user
(
    id                   SERIAL PRIMARY KEY,                      -- 表ID
    role                 VARCHAR(16) NOT NULL,                    -- 身分
    telegram_app_user_id VARCHAR(16) NOT NULL,                    -- telegram app內的user id
    telegram_chat_id     VARCHAR(16) NOT NULL,                    -- telegram user的對話id
    first_name           varchar(25),                             -- 名字
    language_id          INT,                                     -- 語系代碼（例如：'zh-hant', 'en'）
    is_block             boolean     NOT NULL DEFAULT FALSE,      -- 是否為黑名單
    free_points          INT                  DEFAULT 0 NOT NULL, -- 免費積分欄位
    purchased_points     INT                  DEFAULT 0 NOT NULL, -- 付費積分欄位
    last_play_dice_time  TIMESTAMP,                               -- 上次玩色子時間
    has_add_in_channel   boolean     NOT NULL DEFAULT false,      -- 是否加入頻道
    created_at           TIMESTAMPTZ          DEFAULT NOW(),      -- 註冊時間
    updated_at           TIMESTAMPTZ          DEFAULT NOW()
);

-- twitter使用者表
DROP TABLE IF EXISTS twitter_account;
CREATE TABLE twitter_account
(
    id               SERIAL PRIMARY KEY,
    telegram_user_id INT UNIQUE,
    twitter_account  varchar(52) NOT NULL,
    twitter_name     varchar(52) NOT NULL,
    is_premium       BOOLEAN     DEFAULT FALSE,
    created_at       TIMESTAMPTZ DEFAULT NOW()
);

-- 語系表
DROP TABLE IF EXISTS language;
CREATE TABLE language
(
    id            SERIAL PRIMARY KEY,   -- 表ID
    language_code VARCHAR(20) NOT NULL, -- 語系代碼（例如：'zh-hant', 'en'）
    language_name VARCHAR(20) NOT NULL  -- 語系名稱（例如：'繁體中文', 'English')
);

-- 系統積分日誌
DROP TABLE IF EXISTS point_log;
CREATE TABLE point_log
(
    id                         SERIAL PRIMARY KEY,        -- 主鍵
    telegram_user_id           INT          NOT NULL,     -- 用戶ID，對應 user 表
    point_type                 VARCHAR(10)  NOT NULL,     -- 積分類型：免費積分或付費積分
    amount                     INT          NOT NULL,     -- 變動數量（正數增加，負數減少）
    balance_before             INT          NOT NULL,     -- 變動後的積分餘額
    balance_after              INT          NOT NULL,     -- 變動後的積分餘額
    reason                     VARCHAR(255) NOT NULL,     -- 積分變動的原因
    telegram_payment_charge_id TEXT,                      -- telegram 付款ID
    provider_payment_charge_id TEXT,                      -- 提供者交易ID
    created_at                 TIMESTAMPTZ DEFAULT NOW(), -- 記錄時間
    updated_at                 TIMESTAMPTZ DEFAULT NOW()  -- 更新時間
);

-- 多語系文字
DROP TABLE IF EXISTS text;
CREATE TABLE text
(
    id          SERIAL PRIMARY KEY,
    language_id INT  NOT NULL,
    content     TEXT NOT NULL,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    updated_at  TIMESTAMPTZ DEFAULT NOW()
);

-- 公告
DROP TABLE IF EXISTS announcement;
CREATE TABLE announcement
(
    id         SERIAL PRIMARY KEY,
    type       VARCHAR(50) NOT NULL default 'OTHER',
    sequence   INTEGER     NOT NULL DEFAULT 0, -- 排序編號
    created_at TIMESTAMPTZ          DEFAULT NOW(),
    updated_at TIMESTAMPTZ          DEFAULT NOW()
);

-- 公告多語系文字 多對多關聯表
DROP TABLE IF EXISTS announcement_text;
CREATE TABLE announcement_text
(
    announcement_id INT NOT NULL, -- 公告id
    text_id         INT NOT NULL, -- 文字ID
    PRIMARY KEY (announcement_id, text_id)
);

-- 任務主表
DROP TABLE IF EXISTS task;
CREATE TABLE task
(
    id               SERIAL PRIMARY KEY,
    title            VARCHAR(255),
    description      TEXT,
    telegram_user_id INTEGER,
    status           VARCHAR(50),
    created_at       TIMESTAMPTZ DEFAULT NOW(),
    updated_at       TIMESTAMPTZ DEFAULT NOW()
);

-- 任務狀態變更紀錄
DROP TABLE IF EXISTS task_status_log;
CREATE TABLE task_status_log
(
    id         SERIAL PRIMARY KEY,
    task_id    INTEGER,
    status     VARCHAR(50),
    changed_by VARCHAR(50),
    changed_at TIMESTAMPTZ DEFAULT NOW()
);

-- 驗證失敗紀錄
DROP TABLE IF EXISTS verification_log;
CREATE TABLE verification_log
(
    id               SERIAL PRIMARY KEY,
    telegram_user_id INTEGER,
    failed_reason    TEXT,
    failed_at        TIMESTAMPTZ DEFAULT NOW(),
    failed_count     INTEGER     DEFAULT 1
);

-- 黑名單（來源可能是驗證失敗太多）
DROP TABLE IF EXISTS blacklist;
CREATE TABLE blacklist
(
    id               SERIAL PRIMARY KEY,
    telegram_user_id INTEGER,
    reason           TEXT,
    blacklisted_at   TIMESTAMPTZ DEFAULT NOW()
);

-- 任務執行記錄
DROP TABLE IF EXISTS task_execution_log;
CREATE TABLE task_execution_log
(
    id               SERIAL PRIMARY KEY,
    task_id          INTEGER,
    telegram_user_id INTEGER,
    success          BOOLEAN,
    executed_at      TIMESTAMPTZ DEFAULT NOW(),
    notes            TEXT
);
