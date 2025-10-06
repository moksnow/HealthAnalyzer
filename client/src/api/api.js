import { useAuth } from "../context/AuthContext";
import { fetchWithAuth } from "../context/fetchWithAuth";

export async function uploadFile(file, token) {
  const formData = new FormData();
  formData.append("file", file);

  const res = await fetchWithAuth("http://localhost:8080/api/upload", {
    method: "POST",
    body: formData,
    //Setting Content-Type here is not necessary because it is FormData, and the browser automatically sets multipart
  }, token);

  if (!res.ok) {
    const errorText = await res.text();
    throw new Error(errorText);
  }

  return res.json();
}