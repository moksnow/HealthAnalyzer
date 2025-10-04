export const fetchWithAuth = async (url, options = {}, token) => {
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`,
    };
  
    // فقط اگر body از نوع FormData نیست، Content-Type بذار
    if (!(options.body instanceof FormData)) {
      headers["Content-Type"] = "application/json";
    }
  
    const response = await fetch(url, {
      ...options,
      headers,
    });
  
    return response;
  };