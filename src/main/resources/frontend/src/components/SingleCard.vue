<script setup lang="ts">
import axios from "axios";

import {useNotification} from "@kyvg/vue3-notification";

const {notify} = useNotification()

import {useMainStore} from '@/stores/main.ts'
import {ref} from "vue";

let store = useMainStore()
let deleted = ref(false)

interface Card {
  content: string;
  id: string;
  hint: string;
}

const props = defineProps<{
  card: Card;
  search: string;
}>();

async function deleteCard(id: string) {
  if (id.length == 0) {
    return
  }

  try {
    let resp = await axios.post(import.meta.env.VITE_API_URL + '/flashcard/delete', {
      id: id,
    }, {
      headers: {
        'Authorization': store.token
      }
    })

    if (resp.status === 200) {
      deleted.value = true
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

async function updateCard() {
  if (props.card.content.length == 0) {
    return
  }

  try {
    let resp = await axios.post(import.meta.env.VITE_API_URL + '/flashcard/save', {
      id: props.card.id,
      content: props.card.content,
      hint: props.card.hint,
    }, {
      headers: {
        'Authorization': store.token
      }
    })

    if (resp.status === 200) {
      editWindowVisible.value = false
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

const editWindowVisible = ref(false)

function cardClick() {
  if (editWindowVisible.value) {
    editWindowVisible.value = false
    return
  }
  editWindowVisible.value = true
}

function isFiltered(cardText: string) {
  return props.search.length < 3 || cardText.indexOf(props.search) >= 0;
}

</script>

<template>
  <div class="col" v-if="!deleted && isFiltered(card.content)">
    <div class="single-card p-3">
      <p class="card-text">
        {{ card.content }}
      </p>
      <span class="tooltiptext" v-if="card.hint != undefined && card.hint.length > 0">{{ card.hint }}</span>
      <i class="trash-btn bi-trash-fill" @click="deleteCard(card.id)"></i>
      <i class="expand bi-chevron-expand" @click="cardClick"></i>
      <div v-if="editWindowVisible" @click.stop="">
        <input type="text" v-model="card.content" class="form-control mb-1">
        <textarea type="text" v-model="card.hint" class="form-control"></textarea>
        <button class="btn btn-outline-primary mt-2 mb-3" @click="updateCard">Update</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.single-card {
  border-radius: 4px;
  background-color: var(--card-bg);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.15), 0 1px 1px rgba(0, 0, 0, 0.075);
  text-align: center;
  overflow-wrap: break-word;
  position: relative;
}
/* Tooltip text */

.tooltiptext {
  visibility: hidden;
  width: 120px;
  background-color: #555;
  color: #fff;
  text-align: center;
  padding: 5px 0;
  border-radius: 6px;

  /* Position the tooltip text */
  position: absolute;
  z-index: 1;
  bottom: 125%;
  left: 50%;
  margin-left: -60px;

  /* Fade in tooltip */
  opacity: 0;
  transition: opacity 0.7s;
}

.tooltiptext::after {
  content: "";
  position: absolute;
  top: 100%;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: #555 transparent transparent transparent;
}

.card-text:hover + .tooltiptext {
  visibility: visible;
  opacity: 1;
}

.trash-btn {
  visibility: hidden;
  position: absolute;
  bottom: 2px;
  right: 10px;
  color: #8f8f8f;
  opacity: 0;
  transition: opacity 0.3s;
}

.trash-btn:hover {
  cursor: pointer;
  color: #4d4d4d;
}

.expand {
  visibility: hidden;
  position: absolute;
  bottom: 2px;
  right: 50%;
  color: #4d4d4d;
  transform: translateX(50%);
  opacity: 0;
  transition: opacity 0.3s;
}

.expand:hover {
  cursor: pointer;
}

.single-card:hover .trash-btn {
  visibility: visible;
  opacity: 1;
}

.single-card:hover .expand {
  visibility: visible;
  opacity: 1;
}

</style>