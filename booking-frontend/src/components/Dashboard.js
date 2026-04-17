import React, { useEffect, useState } from "react";
import { getDashboard } from "../services/api";
import KPI from "./KPI";
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid
} from "recharts";

export default function Dashboard() {

  const [data, setData] = useState(null);
  const [date, setDate] = useState("");
const formatDate = (d) => d.toISOString().split("T")[0];
  
 useEffect(() => {
  setDate(formatDate(new Date()));
}, []);

  useEffect(() => {
    if (date) {
      fetchData(date);
    }
  }, [date]);

  const fetchData = async (selectedDate) => {
    const res = await getDashboard(selectedDate);
    setData(res.data);
  };
const handlePrevDay = () => {
  const d = new Date(date);
  d.setDate(d.getDate() - 1);
  setDate(formatDate(d));
};
 const handleNextDay = () => {
  const d = new Date(date);
  d.setDate(d.getDate() + 1);
  setDate(formatDate(d));
};
const handleToday = () => {
  setDate(formatDate(new Date()));
};
  if (!data) return <h2>Loading...</h2>;

  return (
    <div style={{ padding: "20px" }}>

      <h1>Restaurant Dashboard</h1>
<div style={{ display: "flex", gap: "10px", marginBottom: "20px" }}>

  <button onClick={handlePrevDay}>⬅️ Prev</button>

  <input
    type="date"
    value={date}
    onChange={(e) => setDate(e.target.value)}
  />

  <button onClick={handleNextDay}>Next ➡️</button>

  <button onClick={handleToday}>Today</button>

</div>
      {/* KPI SECTION */}
      <div style={{ display: "flex", gap: "20px", marginBottom: "30px" }}>
        <KPI title="Total Bookings" value={data.totalBookings} />
        <KPI title="Total Covers" value={data.totalCovers} />
        <KPI title="Avg Party Size" value={data.averagePartySize.toFixed(2)} />
        <KPI title="Table Utilization" value={(data.tableUtilization * 100).toFixed(1) + "%"} />
      </div>

      {/* PEAK HOURS */}
      <h3>Peak Hours</h3>
      <BarChart width={600} height={300} data={data.peakHours}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="hour" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="count" />
      </BarChart>

      {/* LOW DEMAND */}
      <h3>Low Demand Hours</h3>
      <BarChart width={600} height={300} data={data.lowDemandHours}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="hour" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="count" />
      </BarChart>

      {/* TABLE TURNOVER */}
      <h3>Table Turnover</h3>
      <BarChart width={600} height={300} data={data.tableTurnover}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="tableId" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="bookings" />
      </BarChart>

      {/* ZONE PERFORMANCE */}
      <h3>Zone Performance</h3>
      <BarChart width={600} height={300} data={data.zonePerformance}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="zone" />
        <YAxis />
        <Tooltip />
        <Bar dataKey="bookings" />
      </BarChart>

    </div>
  );
}