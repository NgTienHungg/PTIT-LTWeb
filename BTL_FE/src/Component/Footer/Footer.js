import React from "react";
import { Link } from "react-router-dom";
import "./Footer.css";

const Footer = () => {
  return (
    <>
      <footer className="footer">
        {localStorage.getItem("token") === "admin" && (
          <div className="col-2 ml-auto">
            <Link to={`/admin/book/-1`} className="btn btn-primary">
              Add book
            </Link>
          </div>
        )}
      </footer>
    </>
  );
};

export default Footer;
