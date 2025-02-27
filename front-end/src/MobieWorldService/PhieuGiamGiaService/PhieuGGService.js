import axios from "axios";

const PGG_BASE_URL = "http://localhost:8080/phieu-giam-gia";

class PhieuGGService {
  getData() {
    return axios.get(`${PGG_BASE_URL}/data`);
  }
  
  createPgg(pgg) {
    return axios.post(`${PGG_BASE_URL}/addPhieuGiamGia`, pgg);
  }
}

export default new PhieuGGService();