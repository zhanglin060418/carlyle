<template>
  <div ref="emblaNode" class="embla">
    <div ref="emblaViewport" class="embla__viewport">
      <div class="embla__container">
        <div v-for="(element, key) in elements" :key="key" class="embla__slide">
          <van-image lazy-load :src="element" class="embla__slide__img" :alt="element" />
        </div>
      </div>
      <div class="embla__dots" />
    </div>
  </div>
</template>
  
  <script>
  import EmblaCarousel from 'embla-carousel'
  import Autoplay from 'embla-carousel-autoplay'
  
  export default {
    name: 'T3CeCarousel',
  
    props: {
      id: {
        type: Number,
        default: 0
      },
      elements: {
        type: Array,
        default: () => ({})
      },
      autostart: {
        type: Boolean,
        default: true
      },
      controls: {
        type: Boolean,
        default: false
      },
      indicators: {
        type: Boolean,
        default: true
      },
      interval: {
        type: Number,
        default: 3000
      }
    },
  
    mounted() {
      setTimeout(() => {
        this.initCarousel()
      }, 2000);
    },
  
    methods: {
      initCarousel() {
        const viewportNode = this.$refs.emblaViewport
        const options = { loop: true }
        const autoplayOptions = {
          playOnInit: !!this.autostart,
          stopOnInteraction: false,
          delay: parseInt(this.interval) || 4000,
        }
  
        const emblaApi = EmblaCarousel(viewportNode, options, [ Autoplay(autoplayOptions) ])
  
      },
    }
  }
  </script>
  
<style>
  .embla {
    position: relative;
    --slide-spacing: 8px;
    --slide-size: 100%;
    --slide-height: 100%;
    padding: 8px;
  }
  .embla__viewport {
    overflow: hidden;
  }
  .embla__container {
    display: flex;
    flex-direction: row;
    height: auto;
    margin-left: calc(var(--slide-spacing) * -1);
  }
  .embla__slide {
    flex: 0 0 var(--slide-size);
    min-width: 0;
    padding-left: var(--slide-spacing);
    position: relative;
  }
  .embla__slide__img {
    display: block;
    height: var(--slide-height);
    width: 100%;
    object-fit: cover;
  }
  .embla__slide__img img {
    border-radius: 6px;
  }
  /*.embla__slide__number {*/
  /*  width: 4.6rem;*/
  /*  height: 4.6rem;*/
  /*  z-index: 1;*/
  /*  position: absolute;*/
  /*  top: 0.6rem;*/
  /*  right: 0.6rem;*/
  /*  border-radius: 50%;*/
  /*  background-color: rgba(var(--background-site-rgb-value), 0.85);*/
  /*  line-height: 4.6rem;*/
  /*  font-weight: 900;*/
  /*  text-align: center;*/
  /*  pointer-events: none;*/
  /*}*/
  .embla__slide__number > span {
    color: var(--brand-primary);
    background-image: linear-gradient(
        45deg,
        var(--brand-primary),
        var(--brand-secondary)
    );
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-size: 1.6rem;
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
  }
  .embla__dot,
  .embla__button {
    -webkit-appearance: none;
    background-color: transparent;
    touch-action: manipulation;
    display: inline-flex;
    text-decoration: none;
    cursor: pointer;
    border: 0;
    padding: 0;
    margin: 0;
  }
  .embla__dots {
    z-index: 1;
    bottom: 1.2rem;
    position: absolute;
    left: 0;
    right: 0;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .embla__dot {
    width: 0.5rem;
    height: 3rem;
    display: flex;
    align-items: center;
    margin-right: 0.5rem;
    margin-left: 0.5rem;
  }
  .embla__dot:after {
    background: white;
    border-radius: 0.2rem;
    width: 100%;
    height: 0.3rem;
    content: '';
  }
  .embla__dot--selected:after {
    @apply bg-brand;
  }
  .embla__button {
    z-index: 1;
    color: var(--background-site);
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    width: 4rem;
    height: 4rem;
  }
  .embla__button--prev {
    left: -2.2rem;
  }
  .embla__button--next {
    right: -2.2rem;
  }
  .embla__button:disabled {
    opacity: 0.3;
  }
  .embla__button__svg {
    width: 65%;
    height: 65%;
  }
</style>
  