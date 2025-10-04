import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import { BrowserRouter } from "react-router-dom";
import { ResultProvider } from "./context/ResultContext.jsx";
import "./index.css"; // ← MAKE SURE THIS LINE IS PRESENT

ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <ResultProvider>
      <App />
    </ResultProvider>
  </BrowserRouter>
);