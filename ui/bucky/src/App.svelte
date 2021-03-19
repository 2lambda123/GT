<script>
    const inputDataToMatch = {"symbol":"lulu","quantity":500};

    let idToDelete = 0;

    let orderToAdd = {symbol:"",side:"buy",quantity:0,price:0};

    let outputData = [];

    const getAllOrders = async () => {
        const { hostname: location } = window.location;
        const response = await fetch(`http://${location}:8084/order/all/`);
       if (!response.ok) throw Error(response.message);

       try {
          outputData = await response.json();
          console.log(outputData);
       } catch (err) {
         throw err;
       }
     };

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
</main>