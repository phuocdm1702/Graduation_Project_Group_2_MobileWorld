import axios from "axios";

const PGG_BASE_URL = "http://localhost:8080/phieu-giam-gia";
const ADD_PGG_PGGCN_KH_TABLE_URL = "http://localhost:8080/add-phieu-giam-gia";

class PhieuGGService {
  getData() {
    return axios.get(`${PGG_BASE_URL}/data-pgg`);
  }
  
  getDataKH() {
    return axios.get(`${ADD_PGG_PGGCN_KH_TABLE_URL}/data-kh`);
  }
  
  createPgg(pgg) {
    return axios.post(`${ADD_PGG_PGGCN_KH_TABLE_URL}/addPhieuGiamGia`, pgg);
  }
}

export default new PhieuGGService();