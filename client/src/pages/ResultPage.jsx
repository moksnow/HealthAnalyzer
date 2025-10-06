import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ResultContext } from "../context/ResultContext";
import { useAuth } from "../context/AuthContext"; 
import { fetchWithAuth } from "../context/fetchWithAuth";



export default function ResultPage() {
  const { documentId } = useParams();
  const { setResult } = useContext(ResultContext);
  const [extractedText, setText] = useState("");
  const [analysis, setAnalysis] = useState(null);
  const [editableText, setEditableText] = useState("");
  const [analysesList, setAnalysesList] = useState([]);
  const [selectedAnalysis, setSelectedAnalysis] = useState(null);
  const navigate = useNavigate();
  const { token } = useAuth(); 


  useEffect(() => {
    if (!documentId) {
      navigate("/");
      return;
    }

    fetchWithAuth(`http://localhost:8080/api/documents/${documentId}`, {
      method: "GET",
      }, token)
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch result");
        return res.json();
      })
      .then((data) => {
        console.log(data)
        // setLocalResult(data);
        setResult(data); // Update the context as well
        setEditableText(data.extractedText || data.text || ""); // Initialize editableText with a default or initial value
      })
      .catch(() => navigate("/"));

      fetchWithAuth(`http://localhost:8080/api/file-analyses/document/${documentId}`, {
        method: "GET",
        }, token)
      .then((res) => res.json())
      .then(setAnalysesList)
      .catch((err) => console.error("Failed to fetch analysis history:", err));

  }, [documentId]);


  const handleAnalyze = () => {
    fetchWithAuth(`http://localhost:8080/api/file-analyses`, {
      method: "POST",
      body: JSON.stringify({
        uploadedDocumentId: documentId,     
        extractedText: editableText         // Edited text
      }),
    },token)
      .then((res) => {
        if (!res.ok) throw new Error("Analysis failed");
        return res.json();
      })
      .then((analysis) => {
        console.log("✅ Analysis Result:", analysis);
        setAnalysis(analysis);  //  Assuming useState is imported 
        // Refresh analysis list
        fetchWithAuth(`http://localhost:8080/api/file-analyses/document/${documentId}`, {
          method: "GET",
          }, token)
          .then((res) => res.json())
          .then(setAnalysesList);
      })
      .catch((err) => alert("Error during analysis: " + err.message));
  };

  const handleViewAnalysis = (analysisId) => {
    fetchWithAuth(`http://localhost:8080/api/file-analyses/${analysisId}`, {
      method: "GET",
      }, token)
      .then((res) => res.json())
      .then(setSelectedAnalysis)
      .catch((err) => alert("Error fetching analysis result"));
  };

  const handleBack = () => {
    setResult(null);
    navigate("/");
  };

  return (
    <div className="bg-white p-6 rounded shadow max-w-2xl mx-auto space-y-4">
      <h2 className="text-xl font-bold text-green-700">Extracted Text</h2>
      <textarea
        value={editableText}
        onChange={(e) => setEditableText(e.target.value)}
        className="w-full mt-2 p-2 border border-gray-300 rounded h-64 resize-y"
      />
      <div className="flex space-x-4">
        <button
          onClick={handleAnalyze}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
        >
          Analyze
        </button>
        <button
          onClick={handleBack}
          className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
        >
          Back
        </button>
      </div>

      {analysesList.length > 0 && (
        <div className="mt-6">
          <h3 className="text-lg font-semibold text-gray-700 mb-2">Analysis History</h3>
          <ul className="space-y-2">
            {analysesList.map((a) => (
              <li key={a.id} className="flex justify-between items-center bg-gray-100 p-2 rounded">
                <span className="text-sm text-gray-600">
                  {new Date(a.analyzedAt).toLocaleString()}
                </span>
                <button
                  className="text-blue-600 hover:underline text-sm"
                  onClick={() => handleViewAnalysis(a.id)}
                >
                  View Result
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}

      {selectedAnalysis && (
        <div className="mt-6">
          <h3 className="text-lg font-semibold text-purple-600">Selected Analysis Result</h3>
          <pre className="bg-gray-200 p-3 rounded whitespace-pre-wrap break-words">
            {JSON.stringify(selectedAnalysis, null, 2)}
          </pre>
        </div>
      )}

    </div>
  );
}
