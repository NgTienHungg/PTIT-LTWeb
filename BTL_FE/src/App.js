import React from "react";
import { Route, Routes } from "react-router-dom";
import MainPage from "./Component/MainPage/MainPage";
import LoginPage from "./Component/LoginPage/LoginPage";
import RegisterPage from "./Component/RegisterPage/RegisterPage";
import BookClient from "./Component/ClientBookPage/BookClient";
import Book from "./Component/BookDetail/Book";
import OrderList from "./Component/Order/OrderList";

const App = () => {
  return (
    <>
      <Routes>
        <Route path="/library" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/library/book/:id" element={<BookClient />} />
        <Route path="/admin/book/:id" element={<Book />} />
        <Route path="/order" element={<OrderList />} />
      </Routes>
    </>
  );
};

export default App;
