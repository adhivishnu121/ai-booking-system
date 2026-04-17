import { useNavigate } from "react-router-dom";

export default function Navbar() {

  const navigate = useNavigate();

  return (
    <div style={styles.navbar}>

      <div style={styles.left}>
        🍽️ <b>RestroAI Dashboard</b>
      </div>

      <div style={styles.center}>
        <button onClick={() => navigate("/")}>Dashboard</button>
        <button onClick={() => navigate("/tables")}>Tables</button>
        <button onClick={() => navigate("/reservations")}>Reservations</button>
        <button onClick={() => navigate("/analytics")}>Analytics</button>
      </div>

      <div style={styles.right}>
        <button onClick={() => navigate("/settings")}>⚙️</button>
      </div>

    </div>
  );
}

const styles = {
  navbar: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "12px 20px",
    backgroundColor: "#111827",
    color: "white",
  },
  left: {
    fontSize: "18px",
  },
  center: {
    display: "flex",
    gap: "12px",
  },
  right: {
    display: "flex",
  }
};