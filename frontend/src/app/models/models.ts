export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface User {
  id: number;
  name: string;
  email: string;
  role: 'ROLE_USER' | 'ROLE_ADMIN';
  avatarUrl?: string;
  bio?: string;
  createdAt?: string;
}

export interface JwtResponse {
  token: string;
  type: string;
  id: number;
  name: string;
  email: string;
  role: 'ROLE_USER' | 'ROLE_ADMIN';
  avatarUrl?: string;
}

export interface MotivationalQuote {
  id?: number;
  quoteText: string;
  author: string;
  category?: string;
  active?: boolean;
  createdAt?: string;
}

export interface StressAssessmentRequest {
  sleepRating: number;
  workloadRating: number;
  moodRating: number;
  tensionRating: number;
  concentrationRating: number;
  energyRating: number;
  notes?: string;
}

export interface StressAssessmentResult {
  id?: number;
  stressScore: number;
  stressLevel: 'LOW' | 'MODERATE' | 'HIGH';
  summary: string;
  medicalDisclaimer: string;
  recommendations: string[];
  recommendedActivities: string[];
  assessedAt?: string;
}

export interface BreathingSession {
  id?: number;
  technique: 'BOX_BREATHING' | 'SLOW_PACED' | 'FOUR_SEVEN_EIGHT';
  durationSeconds: number;
  cyclesCompleted: number;
  completedAt?: string;
}

export interface MeditationSession {
  id?: number;
  title: string;
  description: string;
  category: string;
  durationMinutes: number;
  audioUrl?: string;
  imageUrl?: string;
  difficulty?: 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
  instructions?: string;
  createdAt?: string;
}

export interface RelaxationResource {
  id?: number;
  title: string;
  category: 'RAIN' | 'OCEAN' | 'FOREST' | 'NIGHT' | 'WHITE_NOISE' | 'MEDITATION_MUSIC' | 'PIANO';
  audioUrl: string;
  iconName?: string;
  description?: string;
  durationSeconds?: number;
  isFeatured?: boolean;
}

export interface MoodEntry {
  id?: number;
  mood: 'HAPPY' | 'CALM' | 'NEUTRAL' | 'ANXIOUS' | 'SAD';
  energyLevel: number;
  notes?: string;
  tags?: string;
  loggedAt?: string;
}

export interface MoodTrend {
  moodDistribution: { [key: string]: number };
  averageEnergy: number;
  dailyTimeline: Array<{
    id: number;
    date: string;
    mood: string;
    energy: number;
    notes?: string;
  }>;
  predominantMood: string;
  insight: string;
}

export interface JournalEntry {
  id?: number;
  title: string;
  content: string;
  moodTag?: string;
  tags?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface WellnessGoal {
  id?: number;
  title: string;
  targetType: 'BREATHING_MINUTES' | 'MEDITATION_MINUTES' | 'MOOD_CHECKINS' | 'JOURNAL_ENTRIES';
  targetValue: number;
  currentValue: number;
  period: 'DAILY' | 'WEEKLY';
  completed: boolean;
  targetDate: string;
  createdAt?: string;
}

export interface DashboardStats {
  welcomeMessage: string;
  dailyQuote: MotivationalQuote;
  todayMood: string;
  todayEnergy?: number;
  currentStreakDays: number;
  totalBreathingMinutes: number;
  totalMeditationMinutes: number;
  journalCount: number;
  assessmentCount: number;
  latestStressLevel: string;
  latestStressScore?: number;
  recentActivities: Array<{
    type: string;
    icon: string;
    title: string;
    detail: string;
    timestamp: string;
  }>;
  activeGoals: Array<{
    id: number;
    title: string;
    current: number;
    target: number;
    percentage: number;
    targetDate: string;
  }>;
}

export interface ProgressReport {
  streakDays: number;
  totalBreathingSessions: number;
  totalBreathingMinutes: number;
  totalMeditationSessions: number;
  totalMeditationMinutes: number;
  totalJournalEntries: number;
  totalMoodCheckins: number;
  weeklyActivityChart: Array<{
    day: string;
    date: string;
    breathingMins: number;
    meditationMins: number;
    checkins: number;
  }>;
  goals: WellnessGoal[];
  overallGoalCompletionPercentage: number;
}

export interface AdminStats {
  totalUsers: number;
  totalMeditationSessions: number;
  totalQuotes: number;
  totalRelaxationResources: number;
  totalBreathingSessionsCompleted: number;
  totalMeditationCompletions: number;
  totalMoodLogs: number;
  totalJournalEntries: number;
}
