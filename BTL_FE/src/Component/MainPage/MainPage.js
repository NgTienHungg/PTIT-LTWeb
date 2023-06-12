import Header from "../Header/Header"
import BookTable from "../AdminBookPage/BookTable";
import ClientPage from "../ClientPage/ClientPage";
import Footer from "../Footer/Footer";

const MainPage = () => {
  return (
    <>
      {/* <Header /> */}
      {
        localStorage.getItem("token") === "admin"
          ? (<BookTable />)
          : (<ClientPage />)
      }
      <Footer />
    </>

  );
};

export default MainPage;