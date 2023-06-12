import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import Header from "../Header/Header";

const OrderList = () => {
  const [orders, setOrders] = useState([]);
  const [bookItems, setBookItems] = useState({});

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const userId = localStorage.getItem("userId");
      const response = await fetch(
        `http://localhost:8080/orders/id?userId=${userId}`
      );
      const data = await response.json();
      setOrders(data);
      fetchBooks(data);
    } catch (error) {
      console.log(error);
    }
  };

  const fetchBooks = async (orders) => {
    try {
      const bookIds = orders.map((order) => order.idBook);
      const bookPromises = bookIds.map((idBook) =>
        fetch(`http://localhost:8080/book/${idBook}`)
      );
      const bookResponses = await Promise.all(bookPromises);
      const bookData = await Promise.all(
        bookResponses.map((response) => response.json())
      );
      const books = bookData.reduce((acc, curr) => {
        acc[curr.id] = curr; // Lưu thông tin sách dựa trên id vào object bookItems
        return acc;
      }, {});
      setBookItems(books);
    } catch (error) {
      console.log(error);
    }
  };

  const handleCancelOrder = (orderId) => {
    const confirmed = window.confirm(
      "Bạn có chắc sẽ huỷ bỏ đơn hàng này?"
    );

    if (confirmed) {
      axios
        .delete(`http://localhost:8080/orders/${orderId}`)
        .then((response) => {
          if (response.status === 200) {
            // Nếu hủy thành công, cập nhật lại danh sách đơn hàng
            fetchOrders();
            console.log(`Order with ID ${orderId} has been canceled.`);
          } else {
            console.log(`Failed to cancel order with ID ${orderId}.`);
          }
        })
        .catch((error) => {
          console.log("Error canceling order:", error);
        });
    }
  };

  return (
    <>
      <Header />
      <br />
      <div className="container">
        <br></br>
        <table className="table table-striped table-bordered">
          <thead className="table-dark">
            <tr>
              <th className="text-center align-middle">Trang bìa</th>
              <th className="text-center align-middle">Tiêu đề</th>
              <th className="text-center align-middle">Tác giả</th>
              <th className="text-center align-middle">Số lượng</th>
              <th className="text-center align-middle"></th>
            </tr>
          </thead>
          <tbody>
            {
              orders.map((order) => (
                <tr key={order.id}>
                  <td className="text-center align-middle">
                    <img src={bookItems[order.idBook]?.cover} alt="Book Cover" style={{ width: "150px" }} />
                  </td>
                  <td className="text-center align-middle">{bookItems[order.idBook]?.title}</td>
                  <td className="text-center align-middle">{bookItems[order.idBook]?.author}</td>
                  <td className="text-center align-middle"> {order.sum}</td>
                  <td className="text-center align-middle">
                    <div className="d-flex flex-column">
                      <Link to={`/library/book/${order.idBook}`} className="btn btn-info mb-1">
                        Chi tiết
                      </Link>
                      <button className="btn btn-danger" onClick={() => handleCancelOrder(order.id)}>
                        Huỷ đơn
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default OrderList;
