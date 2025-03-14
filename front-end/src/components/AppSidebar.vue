<template>
  <div class="flex">
    <!-- Backdrop -->
    <div
      :class="isOpen ? 'block' : 'hidden'"
      @click="isOpen = false"
      class="fixed inset-0 z-20 transition-opacity bg-black opacity-50 lg:hidden"
    ></div>
    <!-- End Backdrop -->

    <div
      :class="isOpen ? 'translate-x-0 ease-out' : '-translate-x-full ease-in'"
      class="fixed inset-y-0 left-0 z-30 w-64 overflow-y-auto transition duration-300 transform bg-white lg:translate-x-0 lg:static lg:inset-0"
    >
      <div class="flex items-center justify-center">
        <div class="flex items-center">
          <img src="@/assets/large-removebg-preview.png" class="w-auto h-40">
        </div>
      </div>

      <nav class="mt-2">
        <p class="pl-4 text-xs font-semibold mb-4 text-gray-700">GENERAL</p>
        <router-link
          class="flex items-center px-6 py-2 mt-4 duration-200 border-l-4"
          :class="[$route.name === 'Dashboard' ? activeClass : inactiveClass]"
          to="/dashboard"
          @click="scrollToSection"
        >
          <svg
            class="w-5 h-5"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
            ></path>
          </svg>
          <span class="mx-4">Thống kê</span>
        </router-link>
        <p class="pl-4 my-2 text-xs font-semibold mb-4 text-gray-700">
          Examples
        </p>

        <div>
          <div class="relative">
            <button
              @click="toggleDropdown('dropdown1')"
              class="flex items-center w-full px-6 py-2 mt-4 duration-200 border-l-4"
              :class="[$route.name === 'Orders' ? activeClass : inactiveClass]"
            >
              <svg
                class="w-7 h-7"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M5 4h1.5L9 16m0 0h8m-8 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm8 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4Zm-8.5-3h9.25L19 7H7.312"
                />
              </svg>
              <span class="mx-4">Bán hàng</span>
              <svg
                class="w-4 h-4 ml-auto transform transition-transform"
                :class="{ 'rotate-180': openDropdown === 'dropdown1' }"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fill-rule="evenodd"
                  d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                  clip-rule="evenodd"
                />
              </svg>
            </button>

            <div v-if="openDropdown === 'dropdown1'" class="ml-8">
              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'Orders' ? activeClass : inactiveClass]"
                to="/orders"
                @click="scrollToSection"
              >
                <svg
                  class="w-6 h-6"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M16 11V9a4 4 0 1 0-8 0v2M5 11h14m-1 0a3 3 0 1 1-6 0M9 11a3 3 0 1 1-6 0m2 8h10a2 2 0 0 0 2-2v-5H5v5a2 2 0 0 0 2 2Z"
                  />
                </svg>
                <span class="mx-4">Tại quầy</span>
              </router-link>

              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'Orders' ? activeClass : inactiveClass]"
                to="/orders"
                @click="scrollToSection"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  class="w-6 h-6"
                  viewBox="0 0 24 24"
                >
                  <path
                    d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5l2.404.961L10.404 2zm3.564 1.426L5.596 5 8 5.961 14.154 3.5zm3.25 1.7-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464z"
                  />
                </svg>
                <span class="mx-4">Đặt Online</span>
              </router-link>
            </div>
          </div>
        </div>

        <router-link
          class="flex items-center px-6 py-2 mt-4 duration-200 border-l-4"
          :class="[$route.name === 'HoaDon' ? activeClass : inactiveClass]"
          to="/hoa-don"
          @click="scrollToSection"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="currentColor"
            class="w-5 h-5"
            viewBox="0 0 16 16"
          >
            <path
              d="M1.92.506a.5.5 0 0 1 .434.14L3 1.293l.646-.647a.5.5 0 0 1 .708 0L5 1.293l.646-.647a.5.5 0 0 1 .708 0L7 1.293l.646-.647a.5.5 0 0 1 .708 0L9 1.293l.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .801.13l.5 1A.5.5 0 0 1 15 2v12a.5.5 0 0 1-.053.224l-.5 1a.5.5 0 0 1-.8.13L13 14.707l-.646.647a.5.5 0 0 1-.708 0L11 14.707l-.646.647a.5.5 0 0 1-.708 0L9 14.707l-.646.647a.5.5 0 0 1-.708 0L7 14.707l-.646.647a.5.5 0 0 1-.708 0L5 14.707l-.646.647a.5.5 0 0 1-.801-.13l-.5-1A.5.5 0 0 1 1 14V2a.5.5 0 0 1 .053-.224l.5-1a.5.5 0 0 1 .367-.27m.217 1.338L2 2.118v11.764l.137.274.51-.51a.5.5 0 0 1 .707 0l.646.647.646-.646a.5.5 0 0 1 .708 0l.646.646.646-.646a.5.5 0 0 1 .708 0l.646.646.646-.646a.5.5 0 0 1 .708 0l.646.646.646-.646a.5.5 0 0 1 .708 0l.509.509.137-.274V2.118l-.137-.274-.51.51a.5.5 0 0 1-.707 0L12 1.707l-.646.647a.5.5 0 0 1-.708 0L10 1.707l-.646.647a.5.5 0 0 1-.708 0L8 1.707l-.646.647a.5.5 0 0 1-.708 0L6 1.707l-.646.647a.5.5 0 0 1-.708 0L4 1.707l-.646.647a.5.5 0 0 1-.708 0z"
            />
            <path
              d="M3 4.5a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 1 1 0 1h-6a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h6a.5.5 0 0 1 0 1h-6a.5.5 0 0 1-.5-.5m8-6a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 0 1h-1a.5.5 0 0 1-.5-.5"
            />
          </svg>
          <span class="mx-4">Quản lý hóa đơn</span>
        </router-link>

        <div>
          <div class="relative">
            <button
              @click="toggleDropdown('dropdown2')"
              class="flex items-center w-full px-6 py-2 mt-4 duration-200 border-l-4"
              :class="[openDropdown === 'dropdown2' ? activeClass : inactiveClass]"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="currentColor"
                class="w-6 h-6"
                viewBox="0 0 16 16"
              >
                <path
                  d="M5.338 1.59a61 61 0 0 0-2.837.856.48.48 0 0 0-.328.39c-.554 4.157.726 7.19 2.253 9.188a10.7 10.7 0 0 0 2.287 2.233c.346.244.652.435.893.533l.077.038c.087.043.155.077.196.102a.5.5 0 0 0 .432 0c.041-.025.11-.06.197-.102l.077-.038c.24-.098.547-.29.893-.533a10.7 10.7 0 0 0 2.287-2.233c1.527-1.997 2.807-5.03 2.253-9.188a.48.48 0 0 0-.328-.39c-.651-.213-1.75-.56-2.837-.855C9.552 1.29 8.531 1.067 8 1.067c-.53 0-1.552.223-2.662.524zM5.072.56C6.157.265 7.31 0 8 0s1.843.265 2.928.56c1.11.3 2.229.655 2.887.87a1.54 1.54 0 0 1 1.044 1.262c.596 4.477-.787 7.795-2.465 9.99a11.8 11.8 0 0 1-2.517 2.453 7 7 0 0 1-1.048.625c-.28.132-.581.24-.829.24s-.548-.108-.829-.24a7 7 0 0 1-1.048-.625 11.8 11.8 0 0 1-2.517-2.453C1.928 10.487.545 7.169 1.141 2.692A1.54 1.54 0 0 1 2.185 1.43 63 63 0 0 1 5.072.56z"
                />
                <path
                  d="M10.854 5.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7.5 7.793l2.646-2.647a.5.5 0 0 1 .708 0z"
                />
              </svg>
              <span class="mx-4">Bảo hành</span>
              <svg
                class="w-4 h-4 ml-auto transform transition-transform"
                :class="{ 'rotate-180': openDropdown === 'dropdown2' }"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fill-rule="evenodd"
                  d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                  clip-rule="evenodd"
                />
              </svg>
            </button>

            <div v-if="openDropdown === 'dropdown2'" class="ml-8">
              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'PhieuBaoHanh' ? activeClass : inactiveClass]"
                to="/phieu-bao-hanh"
                @click="scrollToSection"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  class="w-5 h-5"
                  viewBox="0 0 16 16"
                >
                  <path
                    d="M4 4.5a.5.5 0 0 1 .5-.5H6a.5.5 0 0 1 0 1v.5a.5.5 0 0 1-.5.5h-1a.5.5 0 0 1-.5-.5zm7.5-.5a.5.5 0 0 0 0 1v.5a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5V5a.5.5 0 0 0-.5-.5zM0 4.5A1.5 1.5 0 0 1 1.5 3h13A1.5 1.5 0 0 1 16 4.5V6a.5.5 0 0 1-.5.5 1.5 1.5 0 0 0 0 3 .5.5 0 0 1 .5.5v1.5a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 0 11.5V10a.5.5 0 0 1 .5-.5 1.5 1.5 0 1 0 0-3A.5.5 0 0 1 0 6zm1.5-.5a.5.5 0 0 0-.5.5v1.5a.5.5 0 0 0 .5.5h13a.5.5 0 0 0 .5-.5V4.5a.5.5 0 0 0-.5-.5z"
                  />
                </svg>
                <span class="mx-4">Phiếu bảo hành</span>
              </router-link>
              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'LichSuBaoHanh' ? activeClass : inactiveClass]"
                to="/lich-su-bao-hanh"
                @click="scrollToSection"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  class="w-5 h-5"
                  viewBox="0 0 16 16"
                >
                  <path
                    d="M8.515 1.019A7 7 0 0 0 8 1V0a8 8 0 0 1 .589.022zm-1.03.022A8 8 0 0 1 8 1v1a7 7 0 0 0-.515-.019zm6.004 1.517A7 7 0 0 0 15 8a7 7 0 0 0-.485-2.461zM1.485 2.558A7 7 0 0 0 1 8a7 7 0 0 0 .485 2.461zm11.03 9.923A7 7 0 0 0 15 8a7 7 0 0 0-1.485-4.461zM1 8a7 7 0 0 0 1.485 4.461L1 8zm7-6a6 6 0 1 1 0 12A6 6 0 0 1 8 2zm0 9V8H5V7h4v4h-1z"
                  />
                </svg>
                <span class="mx-4">Lịch sử bảo hành</span>
              </router-link>
            </div>
          </div>
        </div>

        <div>
          <div class="relative">
            <button
              @click="toggleDropdown('dropdown3')"
              class="flex items-center w-full px-6 py-2 mt-4 duration-200 border-l-4"
              :class="[openDropdown === 'dropdown3' ? activeClass : inactiveClass]"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="currentColor"
                class="w-6 h-6"
                viewBox="0 0 16 16"
              >
                <path
                  d="M11 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM5 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"
                />
                <path d="M8 14a1 1 0 1 0 0-2 1 1 0 0 0 0 2" />
              </svg>
              <span class="mx-4">Sản Phẩm</span>
              <svg
                class="w-4 h-4 ml-auto transform transition-transform"
                :class="{ 'rotate-180': openDropdown === 'dropdown3' }"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fill-rule="evenodd"
                  d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                  clip-rule="evenodd"
                />
              </svg>
            </button>

            <div v-if="openDropdown === 'dropdown3'" class="ml-8">
              <router-link
                v-for="(item, index) in productCategories"
                :key="index"
                class="block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === item.route ? activeClass : inactiveClass]"
                :to="item.path"
                @click="scrollToSection"
              >
                <span>{{ item.name }}</span>
              </router-link>
            </div>
          </div>
        </div>

        <div>
          <div class="relative">
            <button
              @click="toggleDropdown('dropdown4')"
              class="flex items-center w-full px-6 py-2 mt-4 duration-200 border-l-4"
              :class="[openDropdown === 'dropdown4' ? activeClass : inactiveClass]"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="currentColor"
                class="w-6 h-6"
                viewBox="0 0 16 16"
              >
                <path
                  d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5m.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1z"
                />
              </svg>
              <span class="mx-4">Quản lý tài khoản</span>
              <svg
                class="w-4 h-4 ml-auto transform transition-transform"
                :class="{ 'rotate-180': openDropdown === 'dropdown4' }"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fill-rule="evenodd"
                  d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                  clip-rule="evenodd"
                />
              </svg>
            </button>

            <div v-if="openDropdown === 'dropdown4'" class="ml-8">
              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'NhanVien' ? activeClass : inactiveClass]"
                to="/nhan-vien"
                @click="scrollToSection"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  class="w-6 h-6"
                  viewBox="0 0 16 16"
                >
                  <path
                    d="M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0M8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4m.256 7a4.5 4.5 0 0 1-.229-1.004H3c.001-.246.154-.986.832-1.664C4.484 10.68 5.711 10 8 10q.39 0 .74.025c.226-.341.496-.65.804-.918Q8.844 9.002 8 9c-5 0-6 3-6 4s1 1 1 1zm3.63-4.54c.18-.613 1.048-.613 1.229 0l.043.148a.64.64 0 0 0 .921.382l.136-.074c.561-.306 1.175.308.87.869l-.075.136a.64.64 0 0 0 .382.92l.149.045c.612.18.612 1.048 0 1.229l-.15.043a.64.64 0 0 0-.38.921l.074.136c.305.561-.309 1.175-.87.87l-.136-.075a.64.64 0 0 0-.92.382l-.045.149c-.18.612-1.048.612-1.229 0l-.043-.15a.64.64 0 0 0-.921-.38l-.136.074c-.561.305-1.175-.309-.87-.87l.075-.136a.64.64 0 0 0-.382-.92l-.148-.045c-.613-.18-.613-1.048 0-1.229l.148-.043a.64.64 0 0 0 .382-.921l-.074-.136c-.306-.561.308-1.175.869-.87l.136.075a.64.64 0 0 0 .92-.382zM14 12.5a1.5 1.5 0 1 0-3 0 1.5 1.5 0 0 0 3 0"
                  />
                </svg>
                <span class="mx-4">Nhân viên</span>
              </router-link>
              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'KhachHang' ? activeClass : inactiveClass]"
                to="/khach-hang"
                @click="scrollToSection"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  class="w-6 h-6"
                  viewBox="0 0 16 16"
                >
                  <path
                    d="M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1zm-7.978-1L7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002-.014.002zM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4m3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0M6.936 9.28a6 6 0 0 0-1.23-.247A7 7 0 0 0 5 9c-4 0-5 3-5 4q0 1 1 1h4.216A2.24 2.24 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816M4.92 10A5.5 5.5 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275ZM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0m3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4"
                  />
                </svg>
                <span class="mx-4">Khách hàng</span>
              </router-link>
            </div>
          </div>
        </div>

        <div>
          <div class="relative">
            <button
              @click="toggleDropdown('dropdown5')"
              class="flex items-center w-full px-6 py-2 mt-4 duration-200 border-l-4"
              :class="[openDropdown === 'dropdown5' ? activeClass : inactiveClass]"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="currentColor"
                class="w-5 h-5"
                viewBox="0 0 16 16"
              >
                <path
                  d="M1.5 3A1.5 1.5 0 0 0 0 4.5V6a.5.5 0 0 0 .5.5 1.5 1.5 0 1 1 0 3 .5.5 0 0 0-.5.5v1.5A1.5 1.5 0 0 0 1.5 13h13a1.5 1.5 0 0 0 1.5-1.5V10a.5.5 0 0 0-.5-.5 1.5 1.5 0 0 1 0-3A.5.5 0 0 0 16 6V4.5A1.5 1.5 0 0 0 14.5 3z"
                />
              </svg>
              <span class="mx-4">Phiếu Giảm giá</span>
              <svg
                class="w-4 h-4 ml-auto transform transition-transform"
                :class="{ 'rotate-180': openDropdown === 'dropdown5' }"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  fill-rule="evenodd"
                  d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                  clip-rule="evenodd"
                />
              </svg>
            </button>

            <div v-if="openDropdown === 'dropdown5'" class="ml-8">
              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'PhieuGiamGia' ? activeClass : inactiveClass]"
                to="/phieu-giam-gia"
                @click="scrollToSection"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  class="w-5 h-5"
                  viewBox="0 0 16 16"
                >
                  <path d="M4 4.85v.9h1v-.9zm7 0v.9h1v-.9zm-7 1.8v.9h1v-.9zm7 0v.9h1v-.9zm-7 1.8v.9h1v-.9zm7 0v.9h1v-.9zm-7 1.8v.9h1v-.9zm7 0v.9h1v-.9z"/>
                  <path d="M1.5 3A1.5 1.5 0 0 0 0 4.5V6a.5.5 0 0 0 .5.5 1.5 1.5 0 1 1 0 3 .5.5 0 0 0-.5.5v1.5A1.5 1.5 0 0 0 1.5 13h13a1.5 1.5 0 0 0 1.5-1.5V10a.5.5 0 0 0-.5-.5 1.5 1.5 0 0 1 0-3A.5.5 0 0 0 16 6V4.5A1.5 1.5 0 0 0 14.5 3zM1 4.5a.5.5 0 0 1 .5-.5h13a.5.5 0 0 1 .5.5v1.05a2.5 2.5 0 0 0 0 4.9v1.05a.5.5 0 0 1-.5.5h-13a.5.5 0 0 1-.5-.5v-1.05a2.5 2.5 0 0 0 0-4.9z"/>
                </svg>
                <span class="mx-4">Phiếu giảm giá</span>
              </router-link>
              <router-link
                class="flex items-center gap-x-3 block px-6 py-2 mt-2 duration-200 border-l-4"
                :class="[$route.name === 'DotGiamGia' ? activeClass : inactiveClass]"
                to="/dot-giam-gia"
                @click="scrollToSection"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  class="w-5 h-5"
                  viewBox="0 0 16 16"
                >
                  <path d="M11 6.5a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-1a.5.5 0 0 1-.5-.5z"/>
                  <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5M1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4z"/>
                </svg>
                <span class="mx-4">Đợt Giảm giá</span>
              </router-link>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useSidebar } from "../hooks/useSidebar";

const { isOpen } = useSidebar();
const openDropdown = ref<string | null>(null);
const activeClass = ref("bg-orange-400 text-white border-orange-300");
const inactiveClass = ref("border-gray-300 text-gray-500 hover:bg-orange-400 hover:text-white");

function toggleDropdown(dropdownId: string) {
  if (openDropdown.value === dropdownId) {
    openDropdown.value = null; // Close if already open
  } else {
    openDropdown.value = dropdownId; // Open new dropdown and close others
  }
  scrollToSection();
}

function scrollToSection() {
  const sidebar = document.querySelector('.fixed.inset-y-0.left-0');
  if (sidebar) {
    sidebar.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
}

const productCategories = [
  {
    name: "Chi Tiết Sản Phẩm",
    path: "/san-pham/chi-tiet",
    route: "Chi Tiết Sản Phẩm",
  },
  { name: "Nhà Sản Xuất", path: "/nha-san-xuat", route: "Nhà Sản Xuất" },
  { name: "Imel", path: "/imel", route: "ImelImel" },
  { name: "Imel đã bán", path: "/imel-da-ban", route: "Imel đã bán" },
  { name: "Dòng Sản Phẩm", path: "/dong-san-pham", route: "Dòng Sản Phẩm" },
  { name: "Màn Hình", path: "/man-hinh", route: "Màn Hình" },
  {
    name: "Công Nghệ Màn Hình",
    path: "/man-hinh/cong-nghe",
    route: "Công Nghệ Màn Hình",
  },
  { name: "RAM", path: "/ram", route: "RAM" },
  { name: "Bộ Nhớ Trong", path: "/bo-nho-trong", route: "Bộ Nhớ Trong" },
  { name: "CPU", path: "/cpu", route: "CPU" },
  { name: "GPU", path: "/gpu", route: "GPU" },
  {
    name: "Thông Số Camera Trước",
    path: "/camera-truoc",
    route: "Thông Số Camera Trước",
  },
  {
    name: "Thông Số Camera Sau",
    path: "/camera-sau",
    route: "Thông Số Camera Sau",
  },
  {
    name: "Chi Tiết Camera Trước",
    path: "/chi-tiet-camera-truoc",
    route: "Chi Tiết Camera Trước",
  },
  {
    name: "Chi Tiết Camera Sau",
    path: "/chi-tiet-camera-sau",
    route: "Chi Tiết Camera Sau",
  },
  { name: "Cụm Camera", path: "/cum-camera", route: "Cụm Camera" },
  { name: "Công Nghệ Sạc", path: "/sac/cong-nghe", route: "Công Nghệ Sạc" },
  {
    name: "Hỗ Trợ Công Nghệ Sạc",
    path: "/sac/ho-tro-cong-nghe",
    route: "Hỗ Trợ Công Nghệ Sạc",
  },
  { name: "Hỗ Trợ Sạc", path: "/sac/ho-tro", route: "Hỗ Trợ Sạc" },
  { name: "Cổng Sạc", path: "/sac/cong-sac", route: "Cổng Sạc" },
];
</script>