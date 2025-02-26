import axios from "axios";

const PGG_BASE_URL = "http://localhost:8080/phieu-giam-gia/data";

class KhachHangService {
  getData() {
    return axios.get(PGG_BASE_URL);
  }
}

export default new KhachHangService();


