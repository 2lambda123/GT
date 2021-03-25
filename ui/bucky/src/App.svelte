<script>
  import Grid from "svelte-grid";
  import gridHelp from "svelte-grid/build/helper/index.mjs";

  const getAllOrders = async () => {
    const { hostname: location } = window.location;
    const response = await fetch(`http://${location}:8084/order/all/`);
    if (!response.ok) throw Error(response.message);

    try {
      items[1].data.outputData = await response.json();
      items[0].data.outputData = items[1].data.outputData;
      items[2].data.outputData = items[1].data.outputData;
      console.log(items[1].data.outputData);
    } catch (err) {
      throw err;
    }

  };

  const { item } = gridHelp;

  let items = [
    {
      id: "Top Header",
      5: item({
        x: 0,
        y: 0,
        w: 2,
        h: 5,
      }),
      3: item({ x: 0, y: 0, w: 3, h: 5 }),
      1: item({ x: 0, y: 0, w: 1, h: 5 }),
      data: {
        outputData: [],
      },
    },

    {
      id: "Info One",
      5: item({
        x: 2,
        y: 0,
        w: 3,
        h: 5,
      }),
      3: item({ x: 0, y: 5, w: 3, h: 5 }),
      1: item({ x: 0, y: 5, w: 1, h: 5 }),
      data: {
        outputData: [],
      },
    },

    {
      id: "Info Two",
      5: item({
        x: 0,
        y: 5,
        w: 5,
        h: 5,
      }),
      3: item({ x: 0, y: 10, w: 3, h: 5 }),
      1: item({ x: 0, y: 10, w: 1, h: 5 }),
      data: {
        outputData: [],
      },
    },
  ];

  const cols = [
    [2000, 5],
    [800, 3],
    [500, 1],
  ];
</script>

<style>
  .centerScreen {
    justify-content: center;
    border: 2px solid red;
  }

  .content {
    min-height: 500px;
    border: solid black 1px;
    padding: 0px;
    margin: 0px;
    border: solid blue 1px;
  }
</style>

<svelte:head>
  <title>Avengers</title>
  <meta name="description" content="Svelte-grid — Example — Add/Remove" />
  <meta name="keywords" content="avengers" />
  <meta name="author" content="Stan" />
</svelte:head>

<div class="centerScreen">

  <button on:click={getAllOrders}>
    Get All Orders
  </button>

  <!-- If item name = "Orders", then it displays a certain element (probably another svelte file) -->
  <Grid bind:items {cols} rowHeight={105} let:index={counter} gap={[30,0]}>
    <div class="content">
      {#each items[counter].data.outputData as item }
        <div>
          <p> {item.id} -> {item.symbol} {item.side} {item.quantity} @ {item.price}</p>
        </div>
      {/each}
    </div>
  </Grid>
</div>

<!-- <script>
    const inputDataToMatch = {"symbol":"lulu","quantity":500};

    let idToDelete = 0;

    let orderToAdd = {symbol:"",side:"buy",quantity:0,price:0};

    let outputData = [];

    const addOrder = async () => {
      const { hostname: location } = window.location;
       const settings = {
         method: 'POST',
         body: JSON.stringify(orderToAdd),
         headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json',
         }
       }

       const response = await fetch(`http://${location}:8084/order/add/`, settings);
       if (!response.ok) throw Error(response.message);

       try {
         const data = await response.data;
         console.log(data);
         getAllOrders();
       } catch (err) {
         throw err;
       }
     };

     const matchOrder = async () => {
      const { hostname: location } = window.location;
       const settings = {
         method: 'POST',
         body: JSON.stringify(inputDataToMatch),
         headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json',
         }
       }

       const response = await fetch(`http://${location}:8084/order/match/`, settings);
       if (!response.ok) throw Error(response.message);

       try {
         const data = await response.data;
         console.log(data);
         getAllOrders();
       } catch (err) {
         throw err;
       }
     };

     const deleteOrder = async () => {
      const { hostname: location } = window.location;
       const settings = {
         method: 'DELETE',
       }

       const response = await fetch(`http://${location}:8084/order/delete/` + idToDelete, settings);
       if (!response.ok) throw Error(response.message);

       try {
         const data = await response.data;
         console.log(data);
         getAllOrders();
       } catch (err) {
         throw err;
       }
     };
</script>

<main>
  <button on:click={getAllOrders}>
    Get All Orders
  </button>

  <p>{orderToAdd.id} -> {orderToAdd.symbol} {orderToAdd.side} {orderToAdd.quantity} @ {orderToAdd.price}</p>

  <label><input type=radio bind:group={orderToAdd.side} value={"buy"}> Buy</label>
  <label><input type=radio bind:group={orderToAdd.side} value={"sell"}> Sell</label>
  <input bind:value={orderToAdd.symbol} placeholder="Symbol">
  <input bind:value={orderToAdd.quantity} placeholder="Quantity">
  <input bind:value={orderToAdd.price} placeholder="Price">
  <button on:click={addOrder}>
	  Add An Order
  </button>

  <button on:click={matchOrder}>
	  Match An Order
  </button>

  <input bind:value={idToDelete} placeholder="Order ID #">
  <button on:click={deleteOrder}>
	  Delete Order #{idToDelete}
  </button>

  {#each outputData as item }
    <div>
      <p> {item.id} -> {item.symbol} {item.side} {item.quantity} @ {item.price}</p>
      <p> --------------- </p>
    </div>
  {/each}
</main> -->
