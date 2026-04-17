import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8081",
});

export const getDashboard = (date) =>
  API.get("/analytics/dashboard", {
    params: { date }
  });

export const getReservations = (date) =>
  API.get("/reservations/by-date", {
    params: { date }
  });