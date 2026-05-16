CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS threads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    model_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subthreads (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    initalMessage TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    thread_id UUID NOT NULL REFERENCES threads(id)
);

CREATE TABLE IF NOT EXISTS messages_table (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    upvote_count INT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMP WITH TIME ZONE,
    subthread_id UUID NOT NULL REFERENCES subthreads(id)
);

CREATE TABLE IF NOT EXISTS message_vote (
    message_id UUID NOT NULL REFERENCES messages_table(id),
    user_id VARCHAR(255) NOT NULL,
    upvoted BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (message_id, user_id)
);

-- =============================================
-- VEHICLE BRANDS & MODELS
-- =============================================
CREATE TABLE IF NOT EXISTS vehicle_brands (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO vehicle_brands (name, created_at) VALUES
('Toyota', NOW()),
('Honda', NOW()),
('Ford', NOW()),
('Volkswagen', NOW()),
('BMW', NOW()),
('Mercedes-Benz', NOW()),
('Audi', NOW()),
('Renault', NOW()),
('Peugeot', NOW()),
('Fiat', NOW()),
('Hyundai', NOW()),
('Kia', NOW()),
('Opel', NOW()),
('Nissan', NOW()),
('SEAT', NOW()),
('Skoda', NOW()),
('Citroën', NOW()),
('Dacia', NOW()),
('Tesla', NOW()),
('Volvo', NOW()),
('Land Rover', NOW()),
('Jeep', NOW()),
('Subaru', NOW()),
('Mazda', NOW()),
('Mitsubishi', NOW()),
('Porsche', NOW()),
('Alfa Romeo', NOW()),
('Suzuki', NOW()),
('TOGG', NOW()),
('Lexus', NOW())
ON CONFLICT (name) DO NOTHING;

-- =============================================
-- SEED DATA
-- =============================================

-- BMW M3 Thread
INSERT INTO threads (user_id, type, model_id, title)
SELECT 'ahmet', 'car-discussion', 'bmw-m3', 'BMW M3 Genel Yorumlar'
WHERE NOT EXISTS (SELECT 1 FROM threads WHERE model_id = 'bmw-m3');

-- Tesla Model S Thread
INSERT INTO threads (user_id, type, model_id, title)
SELECT 'mehmet', 'car-discussion', 'tesla-model-s', 'Tesla Model S Deneyimleri'
WHERE NOT EXISTS (SELECT 1 FROM threads WHERE model_id = 'tesla-model-s');

-- Subthread ekleme
INSERT INTO subthreads (user_id, title, initalMessage, thread_id)
SELECT 'ahmet', 'Motor Performansı', 'BMW M3 motor performansı hakkında tartışma.', t.id
FROM threads t
WHERE t.model_id = 'bmw-m3'
AND NOT EXISTS (SELECT 1 FROM subthreads WHERE title = 'Motor Performansı' AND thread_id = t.id);

INSERT INTO subthreads (user_id, title, initalMessage, thread_id)
SELECT 'mehmet', 'Batarya Ömrü', 'Tesla Model S batarya ömrü hakkında tartışma.', t.id
FROM threads t
WHERE t.model_id = 'tesla-model-s'
AND NOT EXISTS (SELECT 1 FROM subthreads WHERE title = 'Batarya Ömrü' AND thread_id = t.id);

-- Mesaj ekleme
INSERT INTO messages_table (user_id, body, subthread_id)
SELECT 'ahmet', 'BMW M3 gerçekten yüksek performanslı bir araç.', s.id
FROM subthreads s
WHERE s.title = 'Motor Performansı'
AND NOT EXISTS (
    SELECT 1 FROM messages_table
    WHERE user_id = 'ahmet' AND subthread_id = s.id
);

INSERT INTO messages_table (user_id, body, subthread_id)
SELECT 'mehmet', 'Tesla Model S batarya konusunda çok başarılı.', s.id
FROM subthreads s
WHERE s.title = 'Batarya Ömrü'
AND NOT EXISTS (
    SELECT 1 FROM messages_table
    WHERE user_id = 'mehmet' AND subthread_id = s.id
);
