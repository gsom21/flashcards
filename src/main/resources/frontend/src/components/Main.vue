<script setup lang="ts">
import {useMainStore} from '@/stores/main.ts'

let store = useMainStore()

import axios from 'axios'
import {onMounted, ref} from 'vue'
import SingleCard from "@/components/SingleCard.vue";
import {useNotification} from "@kyvg/vue3-notification";

const {notify} = useNotification()


let newFlashcardText = ref('')
let newFlashcardHint = ref('')
let flashcards = ref([])

async function addFlashcard() {
  if (newFlashcardText.value.length === 0) {
    return
  }

  try {
    let response = await axios.post(import.meta.env.VITE_API_URL + "/flashcard/save", {
      content: newFlashcardText.value,
      hint: newFlashcardHint.value,
    }, {
      headers: {
        'Authorization': store.token
      }
    })

    if (response.status === 200) {
      await getFlashcards()
    }

  } catch (error) {
    notify({
      title: "Request error",
      text: error.response.data.error,
      type: "error",
      duration: 5000,
    });
  }
  newFlashcardText.value = ''
  newFlashcardHint.value = ''
  await getFlashcards()
}

async function getFlashcards() {
  try {
    let resp = await axios.get(import.meta.env.VITE_API_URL + '/flashcard/all', {
      headers: {
        'Authorization': store.token
      }
    })

    if (resp.status === 200) {
      flashcards.value = resp.data
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

onMounted(async () => {
  await getFlashcards()
})

</script>


<template>
  <div class="container">
    <div class="row g-4">
      <div class="col-md-6">
        <textarea class="form-control" v-model="newFlashcardText" placeholder="Text"></textarea>
      </div>
      <div class="col-md-6">
        <textarea class="form-control" v-model="newFlashcardHint" placeholder="Hint"></textarea>
      </div>


    </div>
    <div class="row g-4 py-3">
      <div class="col-md-5"></div>
      <div class="col-md-2">
        <button @click="addFlashcard" :class="[store.isNightModeOn ? 'btn-primary' : 'btn-outline-primary']" class="btn " style="width: 100%">Add</button>
      </div>
      <div class="col-md-5"></div>
    </div>

    <div class="row row-cols-1 row-cols-md-3 g-2">
      <SingleCard v-for="card in flashcards" :card="card" :search="newFlashcardText"/>
    </div>

  </div>

</template>

<style scoped>
.col-center {
  margin: 0 auto;
}
</style>