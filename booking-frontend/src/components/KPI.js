import React from "react";

export default function KPI({ title, value }) {
  return (
    <div style={{
      padding: "20px",
      borderRadius: "12px",
      background: "#f5f5f5",
      textAlign: "center",
      width: "200px"
    }}>
      <h4>{title}</h4>
      <h2>{value}</h2>
    </div>
  );
}