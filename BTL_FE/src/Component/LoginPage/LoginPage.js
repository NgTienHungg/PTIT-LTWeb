import "./LoginPage.css";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {

  // khai báo ràng buộc: muốn thay đổi username, phải dùng hàm setUsername
  // useState("") với "" là giá trị mặc định cho username
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    // Gửi yêu cầu đăng nhập thông qua API
    fetch(`http://localhost:8080/user/login?username=${username}&password=${password}`)
      .then((response) => response.json())
      .then((data) => {
        // kiểm tra xem tài khoản tồn tại không 
        if (data.id) {
          localStorage.setItem("token", data.role === 1 ? "admin" : "client");
          localStorage.setItem("username", data.userName);
          localStorage.setItem("userId", data.id);
          navigate("/library");
        }
        else {
          alert("Sai tài khoản hoặc mật khẩu!");
        }
      })
      .catch((error) => alert(error));
  };

  return (
    <div className="login-page">
      <div className="login-form">
        <h2>Login</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={handleUsernameChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={handlePasswordChange}
            />
          </div>
          <button type="submit" className="btn-login">
            Login
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
