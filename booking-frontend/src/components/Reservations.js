import { useEffect, useState } from "react";
import { getReservations } from "../services/api";

export default function Reservations() {

const [data, setData] = useState(null); 
 const [date, setDate] = useState("");
const formatDate = (d) => d.toISOString().split("T")[0];
  
 useEffect(() => {
  setDate(formatDate(new Date()));
}, []);

  
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

  useEffect(() => {
    if (date) {
      fetchReservations(date);
    }
  }, [date]);

  const fetchReservations = async (selectedDate) => {
console.log("Fetching for date:", selectedDate);

  const res = await getReservations(selectedDate);

  console.log("Response:", res.data);
    setData(res.data);
  };

if (data === null) return <h2>Loading...</h2>;

  return (
    <div style={{ padding: "20px" }}>

      <h2>📋 Reservations</h2>

      {/* DATE FILTER */}
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

      {/* TABLE */}
      <table border="1" width="100%" cellPadding="10">
        <thead>
          <tr>
            <th>Name</th>
            <th>Party Size</th>
            <th>Time</th>
            <th>Table</th>
            <th>Zone</th>
            <th>Status</th>
          </tr>
        </thead>

        <tbody>
          {data.map((r) => (
            <tr key={r.id}>
              <td>{r.firstName} {r.lastName}</td>
              <td>{r.partySize}</td>
<td>
  {new Date(r.bookingTime).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: false
  })}
</td>              <td>{r.table?.tableNumber}</td>
              <td>{r.table?.zone}</td>
              <td>{r.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
{data.length === 0 ? (
  <h3>No reservations for this date 📭</h3>
) : (
  <table>
    {/* table */}
  </table>
)}
    </div>
  );
}