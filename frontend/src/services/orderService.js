import api from './api';

// POST /api/v1/orders - Create order
export const createOrder = (orderData) => api.post('/orders', orderData);

// PATCH /api/v1/orders/{id}/payment - Process order payment
export const processPayment = (id, transactionId) => 
    api.patch(`/orders/${id}/payment`, null, { params: { transactionId } });
