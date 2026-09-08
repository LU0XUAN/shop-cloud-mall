import { defineStore } from 'pinia';

export const useLangStore = defineStore('lang', {
    state: () => ({
        language: localStorage.getItem('localeLang') || 'zhCn',
    }),
    actions: {
        changeLang(data) {
            this.language = data;
        },
    },
    persist: true,
});
