const startButton = document.getElementById('startButton');
const statusDiv = document.getElementById('status');

const audioCtx = new (window.AudioContext || window.webkitAudioContext)();

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function playBeep() {
  const oscillator = audioCtx.createOscillator();
  const gainNode = audioCtx.createGain();
  oscillator.type = 'sine';
  oscillator.frequency.value = 440;
  oscillator.connect(gainNode);
  gainNode.connect(audioCtx.destination);
  oscillator.start();
  gainNode.gain.exponentialRampToValueAtTime(0.0001, audioCtx.currentTime + 0.2);
  oscillator.stop(audioCtx.currentTime + 0.2);
}

async function beep(times) {
  for (let i = 0; i < times; i++) {
    playBeep();
    await delay(300);
  }
}

startButton.addEventListener('click', async () => {
  startButton.disabled = true;

  const repDuration = parseInt(document.getElementById('repDuration').value, 10);
  const betweenRepDuration = parseInt(document.getElementById('betweenRepDuration').value, 10);
  const repsPerSet = parseInt(document.getElementById('repsPerSet').value, 10);
  const restTime = parseInt(document.getElementById('restTime').value, 10);
  const sets = parseInt(document.getElementById('sets').value, 10);

  statusDiv.textContent = 'Workout in progress...';

  for (let s = 1; s <= sets; s++) {
    await beep(2); // Set start
    for (let r = 1; r <= repsPerSet; r++) {
      await beep(1); // Rep start
      await delay(repDuration);
      await beep(1); // Rep end
      if (r < repsPerSet) {
        await delay(betweenRepDuration);
      }
    }
    if (s < sets) {
      statusDiv.textContent = `Resting before set ${s + 1}...`;
      await delay(restTime);
    }
  }

  await beep(3); // Exercise completion
  statusDiv.textContent = 'Workout complete!';
  startButton.disabled = false;
});
