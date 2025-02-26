import axios from "axios";

const PGG_BASE_URL = "http://localhost:8080/phieu-giam-gia/data";

class PhieuGGService {
  getData() {
    return axios.get(PGG_BASE_URL);
  }
  
  createPgg(pgg) {
    return axios.post(PGG_BASE_URL, pgg);
  }
}

export default new PhieuGGService();