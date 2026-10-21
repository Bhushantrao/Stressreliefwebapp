USE `stressless_db`;

-- Passwords:
-- Admin password: Admin@123  (BCrypt: $2a$10$eAccYoNOHEqXve8aIWT8Nu3Pk7h0V18bM4Z7Gf2sS.6h8O3v9Xwmi or created dynamically)
-- User password:  User@123   (BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy)

INSERT INTO `users` (`id`, `name`, `email`, `password`, `role`, `avatar_url`, `bio`, `created_at`) VALUES
(1, 'Admin StressLess', 'admin@stressless.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80', 'Platform Wellness Administrator', NOW()),
(2, 'Alex Morgan', 'alex@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_USER', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=200&q=80', 'Software Developer striving for daily peace and focus', NOW())
ON DUPLICATE KEY UPDATE `email`=`email`;

-- Motivational Quotes
INSERT INTO `motivational_quotes` (`id`, `quote_text`, `author`, `category`, `active`) VALUES
(1, 'Feelings come and go like clouds in a windy sky. Conscious breathing is my anchor.', 'Thich Nhat Hanh', 'Mindfulness', TRUE),
(2, 'You don’t have to control your thoughts. You just have to stop letting them control you.', 'Dan Millman', 'Mindset', TRUE),
(3, 'Within you, there is a stillness and a sanctuary to which you can retreat at any time.', 'Hermann Hesse', 'Peace', TRUE),
(4, 'Almost everything will work again if you unplug it for a few minutes, including you.', 'Anne Lamott', 'Rest', TRUE),
(5, 'Tension is who you think you should be. Relaxation is who you are.', 'Chinese Proverb', 'Relaxation', TRUE),
(6, 'Breathe. Let go. And remind yourself that this very moment is the only one you know you have for sure.', 'Oprah Winfrey', 'Presence', TRUE),
(7, 'Peace comes from within. Do not seek it without.', 'Buddha', 'Inner Peace', TRUE),
(8, 'Give your stress wings and let it fly away.', 'Terri Guillemets', 'Letting Go', TRUE)
ON DUPLICATE KEY UPDATE `quote_text`=VALUES(`quote_text`);

-- Guided Meditation Sessions
INSERT INTO `meditation_sessions` (`id`, `title`, `description`, `category`, `duration_minutes`, `audio_url`, `image_url`, `difficulty`, `instructions`) VALUES
(1, '5-Minute Morning Clarity', 'Start your day with a calm, grounded mindset. Release morning tension and set gentle intentions.', 'Mindfulness', 5, 'https://cdn.pixabay.com/download/audio/2022/05/27/audio_1808fbf07a.mp3?filename=meditation-mindfulness-ambient-112191.mp3', 'https://images.unsplash.com/photo-1506126613408-eca07ce68773?auto=format&fit=crop&w=600&q=80', 'BEGINNER', 'Sit comfortably with a straight spine. Close your eyes and focus on the natural rise and fall of your chest.'),
(2, 'Deep Work Stress Release', 'A mid-day mental reset designed to unwind screen fatigue, neck tension, and cognitive overwhelm.', 'Stress', 10, 'https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3?filename=relaxing-light-piano-10775.mp3', 'https://images.unsplash.com/photo-1499209974431-9dddcece7f88?auto=format&fit=crop&w=600&q=80', 'BEGINNER', 'Drop your shoulders down. Inhale deeply through your nose, hold for 2 seconds, and sigh it out through your mouth.'),
(3, 'Evening Sleep Journey', 'Transition gently from a busy day into restorative, peaceful sleep with progressive body scan relaxation.', 'Sleep', 15, 'https://cdn.pixabay.com/download/audio/2022/01/18/audio_d0a13f69d2.mp3?filename=ambient-piano-amp-strings-10711.mp3', 'https://images.unsplash.com/photo-1511295742362-92c96b124e52?auto=format&fit=crop&w=600&q=80', 'INTERMEDIATE', 'Lie comfortably in bed. Bring your awareness slowly from your toes up to your forehead, releasing all muscle engagement.'),
(4, 'Anxiety & Overwhelm Anchor', 'Ground yourself immediately during spikes of worry or racing thoughts using sensory awareness.', 'Anxiety', 8, 'https://cdn.pixabay.com/download/audio/2021/08/09/audio_88447e7046.mp3?filename=peaceful-garden-healing-light-meditation-ambient-3498.mp3', 'https://images.unsplash.com/photo-1470240731273-7821a6eeb6bd?auto=format&fit=crop&w=600&q=80', 'BEGINNER', 'Notice 3 things you can hear right now, 2 physical sensations, and take three deliberate deep belly breaths.'),
(5, 'Focused Breath Awareness', 'Strengthen mental discipline, concentration, and emotional resilience through counted breath observation.', 'Focus', 12, 'https://cdn.pixabay.com/download/audio/2022/11/06/audio_2c2d499878.mp3?filename=calm-meditation-124976.mp3', 'https://images.unsplash.com/photo-1518241353330-0f7941c2d9b5?auto=format&fit=crop&w=600&q=80', 'ADVANCED', 'Count 1 on inhalation, 2 on exhalation up to 10. If the mind wanders, gently return to 1 without self-judgment.')
ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);

-- Relaxation Resources (Sounds & Music)
INSERT INTO `relaxation_resources` (`id`, `title`, `category`, `audio_url`, `icon_name`, `description`, `duration_seconds`, `is_featured`) VALUES
(1, 'Gentle Summer Rain', 'RAIN', 'https://cdn.pixabay.com/download/audio/2022/05/16/audio_db65955613.mp3?filename=soft-rain-ambient-111154.mp3', 'bi-cloud-rain', 'Continuous soft rainfall soothing your nervous system', 1800, TRUE),
(2, 'Pacific Ocean Waves', 'OCEAN', 'https://cdn.pixabay.com/download/audio/2021/09/06/audio_73bb1b590e.mp3?filename=ocean-waves-112906.mp3', 'bi-water', 'Rhythmic coastal tide rolling along smooth pebbles', 2400, TRUE),
(3, 'Deep Pine Forest Birds', 'FOREST', 'https://cdn.pixabay.com/download/audio/2022/04/27/audio_4ebca8e89f.mp3?filename=forest-birds-chirping-nature-sound-110756.mp3', 'bi-tree', 'Breezy canopy rustle and gentle birdsong in secluded woods', 1800, TRUE),
(4, 'Midnight Campfire & Crickets', 'NIGHT', 'https://cdn.pixabay.com/download/audio/2022/03/10/audio_51c6b2ee5d.mp3?filename=campfire-crackling-fireplace-sound-119594.mp3', 'bi-fire', 'Warm crackling embers under a clear starry night sky', 2100, TRUE),
(5, 'Calm Piano & Ambient Pads', 'PIANO', 'https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3?filename=relaxing-light-piano-10775.mp3', 'bi-music-note-beamed', 'Soft melodic acoustic piano over ambient warm pads', 1500, TRUE),
(6, '432 Hz Binaural Healing Drone', 'MEDITATION_MUSIC', 'https://cdn.pixabay.com/download/audio/2022/10/14/audio_993952fce6.mp3?filename=tibetan-singing-bowl-meditation-122416.mp3', 'bi-soundwave', 'Resonant Tibetan singing bowl harmonic frequencies', 1800, TRUE)
ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);

-- Sample Mood Entries for User 2 (Alex)
INSERT INTO `mood_entries` (`user_id`, `mood`, `energy_level`, `notes`, `tags`, `logged_at`) VALUES
(2, 'CALM', 4, 'Started morning with 5-minute breathing exercise. Felt grounded.', 'breathing, morning', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(2, 'NEUTRAL', 3, 'Heavy workload today, but managed to take regular screen breaks.', 'work, busy', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 'ANXIOUS', 2, 'Upcoming deadline created stress. Did box breathing session in the afternoon.', 'deadline, stress', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 'HAPPY', 4, 'Completed project milestone! Went for an evening walk and listened to nature sounds.', 'achievement, walking', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'CALM', 5, 'Relaxing weekend morning. Feeling refreshed and peaceful.', 'weekend, rested', NOW());

-- Sample Journal Entries for User 2 (Alex)
INSERT INTO `journal_entries` (`user_id`, `title`, `content`, `mood_tag`, `tags`, `created_at`) VALUES
(2, 'Finding Balance Amid Deadlines', 'Today was intense with multiple deliverables, but instead of panicking, I took 5 minutes to step outside and do slow-paced breathing. It made a remarkable difference in my focus and patience with my team. Note to self: pausing is not falling behind; it is preparing to move forward with clarity.', 'CALM', 'mindfulness, career, resilience', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 'Small Wins and Gratitude', 'Grateful for good coffee, morning sunshine, and finishing the week on a positive note. Learning to be kind to myself when things do not go exactly according to plan.', 'HAPPY', 'gratitude, perspective', NOW());

-- Sample Breathing Sessions
INSERT INTO `breathing_sessions` (`user_id`, `technique`, `duration_seconds`, `cycles_completed`, `completed_at`) VALUES
(2, 'BOX_BREATHING', 300, 18, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 'FOUR_SEVEN_EIGHT', 240, 12, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 'SLOW_PACED', 360, 24, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'BOX_BREATHING', 300, 18, NOW());

-- Sample Stress Assessment
INSERT INTO `stress_assessments` (`user_id`, `stress_score`, `stress_level`, `sleep_rating`, `workload_rating`, `mood_rating`, `energy_rating`, `answers_json`, `recommendations`, `assessed_at`) VALUES
(2, 42, 'MODERATE', 3, 4, 3, 3, '{"sleep":3,"workload":4,"tension":3,"concentration":3,"mood":3,"recovery":2}', 'Your stress score indicates Moderate Stress. We recommend incorporating daily 5-minute Box Breathing exercises, scheduling evening wind-down routines with ambient ocean sounds, and limiting screen exposure 30 minutes before sleep.', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- Sample Wellness Goals
INSERT INTO `wellness_goals` (`user_id`, `title`, `target_type`, `target_value`, `current_value`, `period`, `completed`, `target_date`) VALUES
(2, 'Daily Breathing Practice (5 mins)', 'BREATHING_MINUTES', 5, 5, 'DAILY', TRUE, CURDATE()),
(2, 'Weekly Guided Meditations', 'MEDITATION_MINUTES', 30, 20, 'WEEKLY', FALSE, DATE_ADD(CURDATE(), INTERVAL 3 DAY)),
(2, 'Daily Mood Check-in', 'MOOD_CHECKINS', 1, 1, 'DAILY', TRUE, CURDATE()),
(2, 'Reflective Journaling (3 entries/wk)', 'JOURNAL_ENTRIES', 3, 2, 'WEEKLY', FALSE, DATE_ADD(CURDATE(), INTERVAL 2 DAY));
