use crate::UniFfiTag;
use async_std::io::ReadExt;
use async_std::net::TcpStream;
use std::sync::mpsc::TryRecvError;
use std::sync::{mpsc, Arc};
use std::thread;
use std::thread::sleep;
use std::time::Duration;
use uniffi::{ConvertError, FfiConverter, Lift, MetadataBuffer, UnexpectedUniFFICallbackError};

#[derive(uniffi::Object,Debug, Copy, Clone)]
pub struct AntiDebugger {}

#[uniffi::export(callback_interface)]
pub trait Callback: Send + Sync {
    fn tes_cb(&self, message: String) -> Result<String, String>;
}


#[uniffi::export]
impl AntiDebugger {
    #[uniffi::constructor]
    pub fn new() -> Arc<Self> {
        Arc::new(AntiDebugger {})
    }

    pub async fn check_with_callback(&self, cb: Box<dyn Callback>) {
        let _ = cb.tes_cb("Hahahah".to_string());
    }

    pub async fn check_port(&self) -> bool {
        let host_port = format!("127.0.0.1:{}", 27047);
        let (tx, rx) = mpsc::channel();

        let td = thread::spawn(move || {
            if let Ok(()) = tx.send(TcpStream::connect(host_port)) {};
        });

        //timer
        async_std::task::sleep(Duration::new(2, 0)).await;

        let result = match rx.try_recv() {
            Ok(_handle) => {
                let r = _handle.await;
                match r {
                    Ok(data) => true,
                    Err(_) => false,
                }
            }
            Err(err) => {
                match err {
                    TryRecvError::Empty => {
                        drop(rx);
                        drop(td);
                        //connecting took more than 2 sec
                        false
                    }
                    TryRecvError::Disconnected => false,
                }
            }
        };
        result
    }
}
