export const fetchWithAuth = async (url, options = {}, token) => {
    const headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`,
    };
  
    // Set Content-Type only if the body is not of type FormData
    if (!(options.body instanceof FormData)) {
      headers["Content-Type"] = "application/json";
    }
  
    const response = await fetch(url, {
      ...options,
      headers,
    });
  
    return response;
  };