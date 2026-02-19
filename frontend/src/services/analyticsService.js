import api from './api';

// GET /api/v1/performance/metrics - Get performance metrics
export const getPerformanceMetrics = () => api.get('/performance/metrics');

// GET /api/v1/performance/cache - Get cache statistics
export const getCacheStats = () => api.get('/performance/cache');
