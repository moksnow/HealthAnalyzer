import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { fetchWithAuth } from "../context/fetchWithAuth";
import parseJwt from "../utils/parseJwt";


export default function ProfilePage() {
  const [activeTab, setActiveTab] = useState("info");
  const [uploadedDocuments, setUploadedDocuments] = useState([]);
  const [selectedDocumentDetails, setSelectedDocumentDetails] = useState(null);
  const [profile, setProfile] = useState(null);
  const [showUserForm, setShowUserForm] = useState(false);
  const [newUser, setNewUser] = useState({ username: "", email: "" });
  const { token } = useAuth();


  const payload = parseJwt(token);
  const userId = payload?.userId;

  useEffect(() => {
    fetchWithAuth(`http://localhost:8080/api/user-profiles/${userId}`, {
      method: "GET",
    }, token)
      .then((res) => res.json())
      .then(setProfile)
      .catch(console.error);
  }, [userId]);

  // Uploading user files
  useEffect(() => {
    if (activeTab === "history") {
      fetchWithAuth(`http://localhost:8080/api/documents/user/${userId}`, {
        method: "GET",
      }, token)
        .then((res) => res.json())
        .then(setUploadedDocuments)
        .catch(console.error);
    }
  }, [activeTab, userId]);

  // Getting details of the selected file
  const handleDocumentClick = (docId) => {
    fetchWithAuth(`http://localhost:8080/api/documents/${docId}/details`, {
      method: "GET",
    }, token)
      .then((res) => res.json())
      .then(setSelectedDocumentDetails)
      .catch(console.error);
  };

  const handleDownload = async (documentId) => {
  try {
    const res = await fetchWithAuth(`http://localhost:8080/api/documents/download/${documentId}`, {
      method: "GET",
    }, token); // Get it from useAuth or pass it via props

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(errorText);
    }

    const blob = await res.blob();
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = `document-${documentId}.pdf`; // Desired file name
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url); // Free up memory
  } catch (err) {
    alert("Error downloading file: " + err.message);
  }
};

  return (
    <div className="p-6 max-w-4xl mx-auto bg-white shadow rounded">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-bold">👤 User profile</h2>
        <button
          className="px-3 py-1 bg-green-500 text-white rounded"
          onClick={() => setShowUserForm(true)}
        >
          + New User
        </button>
      </div>

      {showUserForm && (
        <div className="border p-4 rounded bg-gray-50 mt-4">
          <h3 className="font-semibold mb-2">➕ Create new account</h3>
          <input
            type="text"
            placeholder="Name"
            className="border p-2 w-full mb-2"
            value={newUser.fullName}
            onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
          />
          <input
            type="email"
            placeholder="Email"
            className="border p-2 w-full mb-2"
            value={newUser.email}
            onChange={(e) => setNewUser({ ...newUser, email: e.target.value })}
          />
          {/* <input
            type="text"
            placeholder="Contact"
            className="border p-2 w-full mb-2"
            value={newUser.phoneNumber}
            onChange={(e) => setNewUser({ ...newUser, phoneNumber: e.target.value })}
          /> */}
          <button
            className="bg-blue-500 text-white px-4 py-1 rounded"
            onClick={() => {
              fetchWithAuth("http://localhost:8080/api/user-profiles", {
                method: "POST",
                body: JSON.stringify(newUser),
              }, token)
                .then((res) => res.json())
                .then(() => {
                  setShowUserForm(false);
                  setNewUser({ username: "", email: "" });
                  alert("✅ User successfully added.");
                })
                .catch(console.error);
            }}
          >
            Save
          </button>
        </div>
      )}

      {/* Tabs */}
      <div className="flex border-b mb-4">
        <button
          className={`px-4 py-2 ${activeTab === "info" ? "border-b-2 border-blue-500 font-bold" : ""}`}
          onClick={() => setActiveTab("info")}
        >
          User information
        </button>
        <button
          className={`px-4 py-2 ${activeTab === "history" ? "border-b-2 border-blue-500 font-bold" : ""}`}
          onClick={() => setActiveTab("history")}
        >
          History of files
        </button>
      </div>

      {/* Tab Content */}
      {activeTab === "info" && (
        <div>
          <p>Username: {profile?.username}</p>
          <p>Email: {profile?.email}</p>
          <p>Number of uploaded files: {uploadedDocuments.length}</p>
        </div>
      )}

      {activeTab === "history" && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* List of files */}
          <div className="border p-4 rounded bg-gray-50">
            <h4 className="font-semibold mb-2">📁 Uploaded files:</h4>
            <ul className="space-y-2">
              {uploadedDocuments.map((doc) => (
                <li
                  key={doc.id}
                  className="flex justify-between items-center cursor-pointer hover:bg-gray-100 p-2 rounded"
                >
                  <span
                    onClick={() => handleDocumentClick(doc.id)}
                    className="text-blue-600 hover:underline"
                  >
                    {doc.fileName} - {new Date(doc.uploadDate).toLocaleDateString()}
                  </span>

                  <button
                    onClick={() => handleDownload(doc.id)}
                    className="text-sm text-white bg-blue-500 hover:bg-blue-600 px-2 py-1 rounded"
                  >
                    Download
                  </button>
                </li>
              ))}
            </ul>
          </div>

          {/* Display details of the selected file */}
          {selectedDocumentDetails && (
            <div className="border p-4 rounded bg-gray-100">
              <h4 className="font-semibold">📄 {selectedDocumentDetails.document.fileName}</h4>
              <p className="mt-2"><strong>Extracted text:</strong></p>
              <p className="text-sm bg-white p-2 border rounded">{selectedDocumentDetails.document.extractedText}</p>
              <p className="mt-2"><strong>Upload date:</strong></p>
              <p className="text-sm bg-white p-2 border rounded">{selectedDocumentDetails.document.uploadDate}</p>

              <h5 className="mt-3 font-semibold">Analyses:</h5>
              <ul className="text-sm list-disc pl-4">
                {selectedDocumentDetails.analyses.map((a) => (
                  <li key={a.id}>{a.analysisResult} - {new Date(a.analyzedAt).toLocaleString()}</li>
                ))}
              </ul>

              <h5 className="mt-3 font-semibold">Activities:</h5>
              <ul className="text-sm list-disc pl-4">
                {selectedDocumentDetails.activities.map((act) => (
                  <li key={act.id}>{act.action} - {new Date(act.timestamp).toLocaleString()}</li>
                ))}
              </ul>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
