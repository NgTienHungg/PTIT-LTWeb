import React, { useEffect, useState } from "react";
import "./RegisterPage.css";

const RegisterPage = () => {

  const [user, setUser] = useState({});

  const handleSubmit = (event) => {
    event.preventDefault();

    const newUser = {
      username: user.username,
      password: user.password,
      email: user.email,
    };

    fetch("http://localhost:8080/user/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newUser),
    })
      .then((response) => response.json())
      .then((data) => {
        // kiểm tra xem có thêm user thành công không
        if (data) {
          alert("Đăng ký thành công!");
          window.location.href = "/login";
        }
        else {
          alert("Tài khoản đã tồn tại!")
        }
      })
      .catch((error) => {
        alert("Đăng ký không thành công: " + error.message); // Thông báo lỗi đăng ký
        console.error(error); // In ra lỗi để xử lý
      });
  };


  return (
    <div className="register-page">
      <div className="register-form">
        <h2>Register</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input type="email" id="email" onChange={(e) => setUser({ ...user, email: e.target.value })} />
          </div>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input type="text" id="username" onChange={(e) => setUser({ ...user, username: e.target.value })} />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input type="password" id="password" onChange={(e) => setUser({ ...user, password: e.target.value })} />
          </div>
          <button type="submit" className="register-btn">
            Register
          </button>
        </form>
      </div>
    </div>
  );
};

export default RegisterPage;
