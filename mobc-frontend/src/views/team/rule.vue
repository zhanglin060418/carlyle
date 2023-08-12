<template>
  <div class="content">
    <div class="poput-rule">
      <h2>{{ $t('dw.t143') }}</h2>
      <p class="tips">
        1. {{ $t('dw.t93') }} <span>4%</span> {{ $t('dw.t94') }}
        <span>8%</span> {{ $t('dw.t94a') }}
      </p>
      <p class="tips">
        2. {{ $t('dw.t96') }} <span>2%</span> {{ $t('dw.t97') }}
      </p>
      <p class="tips">
        3.50 {{ $t('dw.t88') }} {{ $t('dw.t89') }} {{ $t('dw.t90')
        }}<span> {{ sym }}1,000,000</span>
      </p>
      <table class="pop-table">
        <tr>
          <th>Jumlah pembeli</th>
          <th>Karunia</th>
        </tr>
        <tr>
          <td>1</td>
          <td>10000</td>
        </tr>
        <tr>
          <td>2</td>
          <td>10000</td>
        </tr>
        <tr>
          <td>5</td>
          <td>50000</td>
        </tr>
        <tr>
          <td>10</td>
          <td>100000</td>
        </tr>
        <tr>
          <td>20</td>
          <td>300000</td>
        </tr>
        <tr>
          <td>50</td>
          <td>1000000</td>
        </tr>
        <tr>
          <td>100</td>
          <td>10000000</td>
        </tr>
      </table>
    </div>
  </div>
</template>
