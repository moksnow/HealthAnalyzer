import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { ResultContext } from "../context/ResultContext";
import { useAuth } from "../context/AuthContext";
import { fetchWithAuth } from "../context/fetchWithAuth";

export default function UploadPage() {
  const [file, setFile] = useState(null);
  const { setResult } = useContext(ResultContext);
  const navigate = useNavigate();
  // const userId = 1; // فرضاً موقت
  const { token } = useAuth();

  const handleUpload = async () => {

    if (!file) {
      alert("Please select a file first.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("userProfileId", 1);

    try {
      const response = await fetchWithAuth("http://localhost:8080/api/documents/upload", {
        method: "POST",
        body: formData,
      }, token);

      if (!response.ok) {
        throw new Error(`Upload failed with status ${response.status}`);
      }

      const result = await response.json();
      setResult(result);
      alert("Upload successful!");
      // فرض اینکه API نتیجه شامل شناسه فایل است: result.id
      navigate(`/result/${result.id}`);
    } catch (error) {
      alert("Upload error: " + error.message);
      console.error(error);
    }
  };

  return (
    <div className="bg-white p-4 rounded shadow max-w-md mx-auto">
      <input type="file" onChange={(e) => setFile(e.target.files[0])} />
      <button
        onClick={handleUpload}
        className="mt-2 px-4 py-2 bg-blue-500 text-white rounded"
      >
        Upload
      </button>
    </div>
  );
}
