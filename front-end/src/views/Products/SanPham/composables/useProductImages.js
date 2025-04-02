import { ref } from 'vue';

export function useProductImages() {
  const showImageSection = ref(false);
  const mainImages = ref({
    front: { file: null, fileName: '', previewUrl: '' },
    back: { file: null, fileName: '', previewUrl: '' },
  });
  const colorImages = ref({});

  const getColorFromName = (colorName) => {
    const vietnameseToEnglishColorMap = {
      đen: 'black',
      bạc: 'silver',
      cam: 'orange',
      đỏ: 'red',
      vàng: 'yellow',
      gold: 'gold',
      xanh: 'green',
      trắng: 'white',
      hồng: 'pink',
      tím: 'purple',
      xám: 'gray',
      nâu: 'brown',
      lam: 'blue',
      'xanh lam': 'blue',
      'xanh lá': 'green',
      'xanh dương': 'blue',
    };
    const colorNameToHex = {
      black: '#000000',
      silver: '#C0C0C0',
      orange: '#FFA500',
      red: '#FF0000',
      yellow: '#FFFF00',
      gold: '#FFD700',
      green: '#008000',
      white: '#FFFFFF',
      pink: '#FF69B4',
      purple: '#800080',
      gray: '#808080',
      brown: '#A52A2A',
      blue: '#0000FF',
    };
    if (!colorName) return '#FFFFFF';
    const normalizedName = colorName.toLowerCase().trim();
    const englishColorName = vietnameseToEnglishColorMap[normalizedName] || normalizedName;
    return colorNameToHex[englishColorName] || '#FFFFFF';
  };

  const handleMainImageUpload = (event, type) => {
    const file = event.target.files[0];
    if (file) {
      if (mainImages.value[type].previewUrl) {
        URL.revokeObjectURL(mainImages.value[type].previewUrl);
      }
      mainImages.value[type] = {
        file,
        fileName: file.name,
        previewUrl: URL.createObjectURL(file),
      };
    }
  };

  const handleColorImageUpload = (event, colorId) => {
    const file = event.target.files[0];
    if (file) {
      if (colorImages.value[colorId]?.previewUrl) {
        URL.revokeObjectURL(colorImages.value[colorId].previewUrl);
      }
      colorImages.value[colorId] = {
        file,
        fileName: file.name,
        previewUrl: URL.createObjectURL(file),
      };
    }
  };

  const resetImages = () => {
    Object.values(mainImages.value).forEach((image) => {
      if (image.previewUrl) {
        URL.revokeObjectURL(image.previewUrl);
      }
    });
    mainImages.value = {
      front: { file: null, fileName: '', previewUrl: '' },
      back: { file: null, fileName: '', previewUrl: '' },
    };
    Object.values(colorImages.value).forEach((image) => {
      if (image?.previewUrl) {
        URL.revokeObjectURL(image.previewUrl);
      }
    });
    colorImages.value = {};
    showImageSection.value = false;
  };

  return {
    showImageSection,
    mainImages,
    colorImages,
    getColorFromName,
    handleMainImageUpload,
    handleColorImageUpload,
    resetImages
  };
}