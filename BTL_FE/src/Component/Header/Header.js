import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./Header.css";

const Header = () => {
  const [isLoggedIn, setLoggedIn] = useState(
    localStorage.getItem("token") !== null
  );

  const handleLogout = () => {
    localStorage.removeItem("token");
    setLoggedIn(false);
    window.location.reload();
  };

  return (
    <header className="header">
      <div className="container">
        <div className="row align-items-center">
          <div className="col-3">
            <div className="d-flex justify-content-start">
              {
                isLoggedIn && (
                  <>
                    <Link to="/library" className="btn btn-warning mr-2">
                      Trang chủ
                    </Link>
                    {
                      localStorage.getItem("token") == "client" && (
                        <Link to="/order" className="btn btn-warning">
                          Đơn hàng
                        </Link>
                      )
                    }
                  </>
                )
              }
            </div>
          </div>

          <div className="col-6 text-center">
            <h1 className="header-title"> {isLoggedIn ? localStorage.getItem("username") : "B20DCCN297 - Nguyễn Tiến Hùng"} </h1>
          </div>

          <div className="col-3 text-right">
            {
              isLoggedIn ? (
                <button className="btn btn-danger" onClick={handleLogout}>
                  Đăng xuất
                </button>
              ) : (
                <div>
                  <Link to="/login" className="btn btn-success mr-2">
                    Đăng nhập
                  </Link>
                  <Link to="/register" className="btn btn-primary">
                    Đăng ký
                  </Link>
                </div>
              )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
