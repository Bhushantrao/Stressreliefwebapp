package com.stressless.config;

import com.stressless.entity.*;
import com.stressless.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MotivationalQuoteRepository quoteRepository;

    @Autowired
    private MeditationSessionRepository meditationRepository;

    @Autowired
    private RelaxationResourceRepository resourceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
        initializeQuotes();
        initializeMeditations();
        initializeRelaxationResources();
    }

    private void initializeUsers() {
        userRepository.findByEmail("admin@stressless.com").ifPresentOrElse(
                admin -> {
                    admin.setPassword(passwordEncoder.encode("Admin@123"));
                    userRepository.save(admin);
                },
                () -> {
                    User admin = new User(
                            "Admin StressLess",
                            "admin@stressless.com",
                            passwordEncoder.encode("Admin@123"),
                            Role.ROLE_ADMIN
                    );
                    admin.setAvatarUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80");
                    admin.setBio("Platform Wellness Administrator");
                    userRepository.save(admin);
                }
        );

        userRepository.findByEmail("alex@example.com").ifPresentOrElse(
                alex -> {
                    alex.setPassword(passwordEncoder.encode("User@123"));
                    userRepository.save(alex);
                },
                () -> {
                    User user = new User(
                            "Alex Morgan",
                            "alex@example.com",
                            passwordEncoder.encode("User@123"),
                            Role.ROLE_USER
                    );
                    user.setAvatarUrl("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=200&q=80");
                    user.setBio("Software Engineer prioritizing daily calm and mindfulness.");
                    userRepository.save(user);
                }
        );
        logger.info("Admin (admin@stressless.com / Admin@123) and Demo User (alex@example.com / User@123) verified.");
    }

    private void initializeQuotes() {
        if (quoteRepository.count() == 0) {
            quoteRepository.save(new MotivationalQuote(
                    "Feelings come and go like clouds in a windy sky. Conscious breathing is my anchor.",
                    "Thich Nhat Hanh", "Mindfulness"));
            quoteRepository.save(new MotivationalQuote(
                    "You don’t have to control your thoughts. You just have to stop letting them control you.",
                    "Dan Millman", "Mindset"));
            quoteRepository.save(new MotivationalQuote(
                    "Within you, there is a stillness and a sanctuary to which you can retreat at any time.",
                    "Hermann Hesse", "Peace"));
            quoteRepository.save(new MotivationalQuote(
                    "Almost everything will work again if you unplug it for a few minutes, including you.",
                    "Anne Lamott", "Rest"));
            quoteRepository.save(new MotivationalQuote(
                    "Tension is who you think you should be. Relaxation is who you are.",
                    "Chinese Proverb", "Relaxation"));
            quoteRepository.save(new MotivationalQuote(
                    "Breathe. Let go. And remind yourself that this very moment is the only one you know you have for sure.",
                    "Oprah Winfrey", "Presence"));
            quoteRepository.save(new MotivationalQuote(
                    "Peace comes from within. Do not seek it without.",
                    "Buddha", "Inner Peace"));
            logger.info("Default motivational quotes initialized.");
        }
    }

    private void initializeMeditations() {
        if (meditationRepository.count() == 0) {
            MeditationSession s1 = new MeditationSession();
            s1.setTitle("5-Minute Morning Clarity");
            s1.setDescription("Start your day with a calm, grounded mindset. Release morning tension and set gentle intentions.");
            s1.setCategory("Mindfulness");
            s1.setDurationMinutes(5);
            s1.setAudioUrl("https://cdn.pixabay.com/download/audio/2022/05/27/audio_1808fbf07a.mp3?filename=meditation-mindfulness-ambient-112191.mp3");
            s1.setImageUrl("https://images.unsplash.com/photo-1506126613408-eca07ce68773?auto=format&fit=crop&w=600&q=80");
            s1.setDifficulty(MeditationSession.Difficulty.BEGINNER);
            s1.setInstructions("Sit comfortably with a straight spine. Close your eyes and focus on the natural rise and fall of your chest.");
            meditationRepository.save(s1);

            MeditationSession s2 = new MeditationSession();
            s2.setTitle("Deep Work Stress Release");
            s2.setDescription("A mid-day mental reset designed to unwind screen fatigue, neck tension, and cognitive overwhelm.");
            s2.setCategory("Stress");
            s2.setDurationMinutes(10);
            s2.setAudioUrl("https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3?filename=relaxing-light-piano-10775.mp3");
            s2.setImageUrl("https://images.unsplash.com/photo-1499209974431-9dddcece7f88?auto=format&fit=crop&w=600&q=80");
            s2.setDifficulty(MeditationSession.Difficulty.BEGINNER);
            s2.setInstructions("Drop your shoulders down. Inhale deeply through your nose, hold for 2 seconds, and sigh it out through your mouth.");
            meditationRepository.save(s2);

            MeditationSession s3 = new MeditationSession();
            s3.setTitle("Evening Sleep Journey");
            s3.setDescription("Transition gently from a busy day into restorative, peaceful sleep with progressive body scan relaxation.");
            s3.setCategory("Sleep");
            s3.setDurationMinutes(15);
            s3.setAudioUrl("https://cdn.pixabay.com/download/audio/2022/01/18/audio_d0a13f69d2.mp3?filename=ambient-piano-amp-strings-10711.mp3");
            s3.setImageUrl("https://images.unsplash.com/photo-1511295742362-92c96b124e52?auto=format&fit=crop&w=600&q=80");
            s3.setDifficulty(MeditationSession.Difficulty.INTERMEDIATE);
            s3.setInstructions("Lie comfortably in bed. Bring your awareness slowly from your toes up to your forehead, releasing all muscle engagement.");
            meditationRepository.save(s3);

            MeditationSession s4 = new MeditationSession();
            s4.setTitle("Anxiety & Overwhelm Anchor");
            s4.setDescription("Ground yourself immediately during spikes of worry or racing thoughts using sensory awareness.");
            s4.setCategory("Anxiety");
            s4.setDurationMinutes(8);
            s4.setAudioUrl("https://cdn.pixabay.com/download/audio/2021/08/09/audio_88447e7046.mp3?filename=peaceful-garden-healing-light-meditation-ambient-3498.mp3");
            s4.setImageUrl("https://images.unsplash.com/photo-1470240731273-7821a6eeb6bd?auto=format&fit=crop&w=600&q=80");
            s4.setDifficulty(MeditationSession.Difficulty.BEGINNER);
            s4.setInstructions("Notice 3 things you can hear right now, 2 physical sensations, and take three deliberate deep belly breaths.");
            meditationRepository.save(s4);

            logger.info("Default meditation sessions initialized.");
        }
    }

    private void initializeRelaxationResources() {
        if (resourceRepository.count() == 0) {
            resourceRepository.save(new RelaxationResource(
                    "Gentle Summer Rain",
                    RelaxationResource.Category.RAIN,
                    "https://cdn.pixabay.com/download/audio/2022/05/16/audio_db65955613.mp3?filename=soft-rain-ambient-111154.mp3",
                    "bi-cloud-rain",
                    "Continuous soft rainfall soothing your nervous system"
            ));

            resourceRepository.save(new RelaxationResource(
                    "Pacific Ocean Waves",
                    RelaxationResource.Category.OCEAN,
                    "https://cdn.pixabay.com/download/audio/2021/09/06/audio_73bb1b590e.mp3?filename=ocean-waves-112906.mp3",
                    "bi-water",
                    "Rhythmic coastal tide rolling along smooth pebbles"
            ));

            resourceRepository.save(new RelaxationResource(
                    "Deep Pine Forest Birds",
                    RelaxationResource.Category.FOREST,
                    "https://cdn.pixabay.com/download/audio/2022/04/27/audio_4ebca8e89f.mp3?filename=forest-birds-chirping-nature-sound-110756.mp3",
                    "bi-tree",
                    "Breezy canopy rustle and gentle birdsong in secluded woods"
            ));

            resourceRepository.save(new RelaxationResource(
                    "Midnight Campfire & Crickets",
                    RelaxationResource.Category.NIGHT,
                    "https://cdn.pixabay.com/download/audio/2022/03/10/audio_51c6b2ee5d.mp3?filename=campfire-crackling-fireplace-sound-119594.mp3",
                    "bi-fire",
                    "Warm crackling embers under a clear starry night sky"
            ));

            resourceRepository.save(new RelaxationResource(
                    "Calm Piano & Ambient Pads",
                    RelaxationResource.Category.PIANO,
                    "https://cdn.pixabay.com/download/audio/2022/03/15/audio_c8c8a73467.mp3?filename=relaxing-light-piano-10775.mp3",
                    "bi-music-note-beamed",
                    "Soft melodic acoustic piano over ambient warm pads"
            ));

            resourceRepository.save(new RelaxationResource(
                    "432 Hz Binaural Healing Drone",
                    RelaxationResource.Category.MEDITATION_MUSIC,
                    "https://cdn.pixabay.com/download/audio/2022/10/14/audio_993952fce6.mp3?filename=tibetan-singing-bowl-meditation-122416.mp3",
                    "bi-soundwave",
                    "Resonant Tibetan singing bowl harmonic frequencies"
            ));

            logger.info("Default relaxation resources initialized.");
        }
    }
}
