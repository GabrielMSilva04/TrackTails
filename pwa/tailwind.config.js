/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  daisyui: {
    themes: [
      {
        mytheme: {


          "primary": "#7bb37b",


          "secondary": "#4d7c0f",


          "accent": "#a3e635",


          "neutral": "#022c22",


          "base-100": "#fff9f0",


          "info": "#0072ff",


          "success": "#00c896",


          "warning": "#d59100",


          "error": "#ff6364",
        },
      },
    ],
  },

  plugins: [
    require('daisyui'),
  ],
}

