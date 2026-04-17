import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Dashboard from "./components/Dashboard";
import Reservations from "./components/Reservations";
function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/tables" element={<div>Tables Page</div>} />
        <Route path="/reservations" element={<Reservations/>}/>
        <Route path="/analytics" element={<div>Analytics Page</div>} />
        <Route path="/settings" element={<div>Settings Page</div>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;