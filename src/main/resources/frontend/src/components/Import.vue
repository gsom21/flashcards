<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

import {useNotification} from "@kyvg/vue3-notification";

const {notify} = useNotification()

import {useMainStore} from '@/stores/main.ts'

let store = useMainStore()


const newFlashcardsText = ref('')

async function importFlashcards() {
  if (newFlashcardsText.value.length == 0) {
    return
  }

  try {
    let resp = await axios.post(import.meta.env.VITE_API_URL + '/flashcard/import', {
      text: newFlashcardsText.value,
    }, {
      headers: {
        'Authorization': store.token
      }
    })

    if (resp.status === 200) {
      newFlashcardsText.value = ''
      notify({
        title: "Success",
        text: "flashcards successfully imported",
        type: "success",
        duration: 5000,
      });
    }

  } catch (error) {
    notify({
      title: "Request error",
      text: error.response.data.error,
      type: "error",
      duration: 5000,
    });
  }
}

</script>

<template>
  <div class="container">
    <div class="row g-4">
      <div class="col-md-12">
        <textarea class="form-control" v-model="newFlashcardsText" placeholder="Text"></textarea>
      </div>


    </div>
    <div class="row g-4 py-3">
      <div class="col-md-2">
        <button @click="importFlashcards" class="btn btn-outline-primary" style="width: 100%">Import</button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>