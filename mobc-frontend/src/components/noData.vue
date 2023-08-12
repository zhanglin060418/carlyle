<template>
  <div class="noData">
    <div class="no-content">
      <slot></slot>
      <img src="static/assets/image/nodata.png" alt="" />
      <template v-if="$route.path=='/more'">
        <p>{{$t('sa.txt313')}}<br>{{$t('sa.txt314')}}</p>
        <base-btn :btnTitle="$t('dw.t177')" :disabled="false" @btnClick="$router.replace('/invite')"></base-btn>
      </template>
      <p v-else>{{ $t('sys.noData') }}</p>
    </div>
  </div>
</template>
